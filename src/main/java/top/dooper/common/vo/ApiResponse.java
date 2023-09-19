package top.dooper.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Integer code;
    private String massage;
    private T data;

    public static <T> ApiResponse<T> success(){
        return new ApiResponse<>(200, "Success", null);
    }
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(200, "Success", data);
    }
    public static <T> ApiResponse<T> success(String msg, T data){
        return new ApiResponse<>(200, msg, data);
    }
    public static <T> ApiResponse<T> error(int code, String msg) { return new ApiResponse<>(code, msg, null); }
}
