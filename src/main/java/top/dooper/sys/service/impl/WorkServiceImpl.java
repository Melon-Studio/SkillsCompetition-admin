package top.dooper.sys.service.impl;

import top.dooper.sys.entity.Work;
import top.dooper.sys.mapper.WorkMapper;
import top.dooper.sys.service.IWorkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AbuLan
 * @since 2023-09-14
 */
@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements IWorkService {

}
