package MakeUs.Moira.util.response;

import MakeUs.Moira.util.response.model.CommonResult;
import MakeUs.Moira.util.response.model.ListResult;
import MakeUs.Moira.util.response.model.SingleResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    // 공통 결과를 처리하는 메소드
    private void setCommonResult(CommonResult result, boolean isSuccess, int code, String msg) {
        result.setSucceed(isSuccess);
        result.setCode(code);
        result.setMsg(msg);
    }

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> mappingSingleResult(T data, String msg) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setCommonResult(result, true, 200, msg);
        return result;
    }
    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> mappingListResult(List<T> list, String msg) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setCommonResult(result, true, 200, msg);
        return result;
    }
    // 결과만 처리하는 메소드 - 성공
    public CommonResult mappingSuccessCommonResultOnly(String msg) {
        CommonResult result = new CommonResult();
        setCommonResult(result, true, 200, msg);
        return result;
    }

    // 결과만 처리하는 메소드 - 실패
    public CommonResult mappingFailCommonResultOnly(int errorcode, String msg) {
        CommonResult result = new CommonResult();
        setCommonResult(result, false, errorcode, msg);
        return result;
    }
}
