package top.dooper.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import top.dooper.common.vo.HomeWork;
import top.dooper.sys.entity.User;
import top.dooper.sys.entity.Work;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AbuLan
 * @since 2023-11-20
 */
public interface IWorkService extends IService<Work> {

    Map<String, String> getPage(String sid);

    List<HomeWork> getList();

    Integer uploadFile(MultipartFile file, String token);

    Integer addWorkInfo(String sid);
    Integer addWorkInfo(String name, String token);

    Map<String, Boolean> hasWorkInfo(String token);

    Integer deleteWork(String token);

    List<HomeWork> getWorkInfo(String token);


    Page<Work> pages(String token, Integer page, String keyword);

    Integer announced(String token);

    Map<String, String> getWorkCode(String token, String sid);

    Map<String, String> getFolderHierarchy(String token, String sid);
}
