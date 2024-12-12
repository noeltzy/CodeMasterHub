package com.zhongyuan.codemasterhub.service.impl;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhongyuan.codemasterhub.common.ErrorCode;
import com.zhongyuan.codemasterhub.common.RedisObject;
import com.zhongyuan.codemasterhub.exception.ThrowUtils;
import com.zhongyuan.codemasterhub.mapper.CourseMapper;
import com.zhongyuan.codemasterhub.model.VO.CourseTagVo;
import com.zhongyuan.codemasterhub.model.VO.CourseVo;
import com.zhongyuan.codemasterhub.model.domain.Course;
import com.zhongyuan.codemasterhub.model.dto.course.CourseAddRequest;
import com.zhongyuan.codemasterhub.model.dto.course.CourseQueryRequest;
import com.zhongyuan.codemasterhub.model.dto.course.CourseUpdateRequest;
import com.zhongyuan.codemasterhub.service.CourseService;
import com.zhongyuan.codemasterhub.service.CourseTagService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.zhongyuan.codemasterhub.constant.RedisKeyConstant.*;

/**
 * @author Windows11
 * @description 针对表【cmh_courses(课程表)】的数据库操作Service实现
 * @createDate 2024-12-09 21:10:40
 */

@Slf4j
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
        implements CourseService {
    @Resource
    CourseTagService courseTagService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;




    private Map<String, CourseTagVo> getTagMap() {
        //查询全部Tags

        // 先查询缓存
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(TAG_MAP_REDIS);
        Map<String, CourseTagVo> tagMap = entries.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey().toString(),
                entry -> (CourseTagVo) entry.getValue()
        ));
        if (!tagMap.isEmpty()) {
            log.info("tagMap from Redis");
            return tagMap;
        }
        //缓存不存在查询数据库 且将数据写入缓存 更新Tag的时候就直接删除缓存（先操作数据库,后操作缓存）
        List<CourseTagVo> allTagList = courseTagService.getAllTagVo();
        tagMap = allTagList.stream().
                collect(Collectors.toMap(tag -> tag.getId().toString(), tag -> tag));

        redisTemplate.opsForHash().putAll(TAG_MAP_REDIS, tagMap);
        return tagMap;
    }

    @Override
    public CourseVo getVoById(Long id) {
        // 先走Redis
        RedisObject courseVoRedisObject = (RedisObject) redisTemplate.opsForValue().get(COURSE_DETAIL_REDIS_PREFIX + id);
        // 查询到数据且没过期
        if (courseVoRedisObject != null && courseVoRedisObject.getExpired().isAfter(LocalDateTime.now())) {
            // 可以有空对象返回
            return (CourseVo) courseVoRedisObject.getObj();
        }
        // TODO：Redis互斥锁,setNx实现，开启一个本地新线程完成缓存重建
        // 没查到就走数据库
        Course course = this.getById(id);
        Map<String, CourseTagVo> tagMap = this.getTagMap();
        CourseVo courseVo = CourseVo.ObjToVo(course, tagMap);
        // 缓存,包括可以缓存空数据
        this.save2RedisWithLogicExpired(courseVo, COURSE_DETAIL_REDIS_PREFIX + id,
                COURSE_DETAIL_REDIS_EXPIRED_TIME);
        return courseVo;
    }

    /**
     * 将数据缓存到Redis Hash结构中
     *
     * @param data            数据
     * @param key             Key
     * @param logicExpireTime 逻辑过期时间,现在过后XX秒
     * @param <T>             存储数据的类型
     */
    private <T> void save2RedisWithLogicExpired(T data, String key, long logicExpireTime) {
        RedisObject<T> redisCourseVo = new RedisObject<>();
        redisCourseVo.setExpired(LocalDateTime.now().plusMinutes(logicExpireTime));
        redisCourseVo.setObj(data);
        redisTemplate.opsForValue().set(key, redisCourseVo, logicExpireTime + 3, TimeUnit.MINUTES);
    }

    @Override
    public List<CourseVo> getCourseVoByIdList(List<Long> favoriteCourseIdList) {
        LambdaQueryWrapper<Course> courseLambdaQueryWrapper = new LambdaQueryWrapper<>();

        courseLambdaQueryWrapper.in(Course::getId, favoriteCourseIdList);
        List<Course> courseList = this.list(courseLambdaQueryWrapper);

        Map<String, CourseTagVo> tagsMap = getTagMap();
        return courseList.stream().map(course -> CourseVo.ObjToVo(course, tagsMap))
                .toList();
    }

    @Override
    public boolean existById(Long id) {
        Long count = this.lambdaQuery().eq(Course::getId, id).count();
        return count > 0;
    }


    @Override
    public void add(CourseAddRequest addCourseRequest) {
        Course course = new Course();
        BeanUtils.copyProperties(addCourseRequest, course);
        this.checkRequest(course, false);
        ThrowUtils.throwIf(!this.save(course), ErrorCode.SYSTEM_ERROR);
    }

    @Override
    public void updateByAdmin(CourseUpdateRequest updateCourseRequest) {
        Course course = new Course();
        BeanUtils.copyProperties(updateCourseRequest, course);
        this.checkRequest(course, true);
        ThrowUtils.throwIf(!this.updateById(course), ErrorCode.SYSTEM_ERROR);
    }

    @Override
    public Page<Course> queryList(CourseQueryRequest listCourseRequest) {
        int current = listCourseRequest.getCurrent();
        int size = listCourseRequest.getPageSize();

        // 查询Course列表
        return this.page(new Page<>(current, size),
                this.getQueryWrapper(listCourseRequest));
    }


    @Override
    public Page<CourseVo> queryVoList(CourseQueryRequest courseQueryRequest) {

        int current = courseQueryRequest.getCurrent();
        int size = courseQueryRequest.getPageSize();
        Page<Course> coursePage = this.queryList(courseQueryRequest);
        Map<String, CourseTagVo> tagsMap = getTagMap();
        // 转化成Vo
        List<CourseVo> list = coursePage.getRecords().stream().
                map(course -> CourseVo.ObjToVo(course, tagsMap))
                .toList();
        // 封装成VoPage
        Page<CourseVo> courseVoPage = new Page<>(current, size, coursePage.getTotal());
        courseVoPage.setRecords(list);
        return courseVoPage;
    }

    private void checkRequest(Course course, boolean update) {
        String name = course.getName();

        // TODO 添加讲师表之后验证
        Long instructorId = course.getInstructorId();

        String tags = course.getTags();
        if(tags!=null) {
            String regex = "^(\\d+(,\\d+)*)?$";
            ThrowUtils.throwIf(!tags.matches(regex), ErrorCode.PARAMS_ERROR,"标签格式错误");
        }

        String coverImage = course.getCoverImage();
        String videoUrl = course.getVideoUrl();

        Integer duration = course.getDuration();
        if(duration!=null){
            ThrowUtils.throwIf(duration <= 0, ErrorCode.PARAMS_ERROR,"视频长度设置有误");
        }


        String description = course.getDescription();


        Integer status = course.getStatus();
        if(status!=null){
            ThrowUtils.throwIf(status!=1&&status!=0,ErrorCode.PARAMS_ERROR,"课程状态之能为0|1");
        }

        BigDecimal price = course.getPrice();

        if (price != null) {
            // 价格不能小于 0
            ThrowUtils.throwIf(BigDecimal.ZERO.compareTo(price) > 0, ErrorCode.PARAMS_ERROR);
        }

        // 如果是更新操作,最起码更新的数据要存在,数据库操作放在最后
        if (update) {
            Long id = course.getId();
            Long count = this.lambdaQuery().eq(Course::getId, id).count();
            ThrowUtils.throwIf(count == 0, ErrorCode.NOT_FOUND_ERROR);
        }
    }


    private LambdaQueryWrapper<Course> getQueryWrapper(CourseQueryRequest courseQueryRequest) {
        String tags = courseQueryRequest.getTags();
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(tags)) {
            String[] tagList = tags.split(",");
            queryWrapper.and(wrapper -> {
                for (String tag : tagList) {
                    wrapper.like(Course::getTags, tag);
                }
            });
        }
        return queryWrapper;
    }
}




