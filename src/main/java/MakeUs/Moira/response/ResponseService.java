package MakeUs.Moira.response;

import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    // 공통 결과를 처리하는 메소드
    private void setCommonResult(CommonResult result, boolean isSuccess, String msg) {
        result.setSuccess(isSuccess);
        result.setMsg(msg);
    }

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> mappingSingleResult(T data, String msg) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setCommonResult(result, true, msg);
        return result;
    }
    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> mappingListResult(List<T> list, String msg) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setCommonResult(result, true, msg);
        return result;
    }
    // 결과만 처리하는 메소드 - 성공
    public CommonResult mappingSuccessCommonResultOnly(String msg) {
        CommonResult result = new CommonResult();
        setCommonResult(result, true, msg);
        return result;
    }

    // 결과만 처리하는 메소드 - 실패
    public CommonResult mappingFailCommonResultOnly(String msg) {
        CommonResult result = new CommonResult();
        setCommonResult(result, false, msg);
        return result;
    }
}
