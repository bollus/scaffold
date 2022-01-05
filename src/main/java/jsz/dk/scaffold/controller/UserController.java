package jsz.dk.scaffold.controller;

import jsz.dk.scaffold.common.annotations.NeedLogin;
import jsz.dk.scaffold.common.annotations.OperationLogDetail;
import jsz.dk.scaffold.common.controller.BaseController;
import jsz.dk.scaffold.common.entity.CustomException;
import jsz.dk.scaffold.common.entity.ResponseParent;
import jsz.dk.scaffold.entity.*;
import jsz.dk.scaffold.entity.dto.GoogleDTO;
import jsz.dk.scaffold.entity.dto.GoogleLoginDTO;
import jsz.dk.scaffold.entity.dto.UserDTO;
import jsz.dk.scaffold.entity.vo.GoogleSecretVO;
import jsz.dk.scaffold.entity.vo.UserLoginVO;
import jsz.dk.scaffold.enums.OperationType;
import jsz.dk.scaffold.enums.OperationUnit;
import jsz.dk.scaffold.enums.ResponseCode;
import jsz.dk.scaffold.services.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.controller
 * @ClassName: UserController
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/02 18:18
 */
@RestController
@RequestMapping("i/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @OperationLogDetail(detail = "注册用户", level = 3, operationUnit = OperationUnit.USER, operationType = OperationType.INSERT)
    @ResponseBody
    @PostMapping("reg")
    public ResponseParent<User> registry(@RequestBody UserDTO dto){
        try {
            return ResponseParent.succeed("注册成功", userService.registry(dto));
        } catch (CustomException ce) {
            return ResponseParent.fail(ResponseCode.CUSTOM_FAILED.getCode(), ce.getMsg());
        }
    }

    @OperationLogDetail(detail = "用户登陆", level = 3, operationUnit = OperationUnit.GOOGLE, operationType = OperationType.SELECT)
    @PostMapping("/login")
    @ResponseBody
    public ResponseParent<UserLoginVO> login(@RequestBody UserDTO dto) {
        try {
            return ResponseParent.succeed("登陆成功", userService.login(dto));
        } catch (CustomException ce) {
            return ResponseParent.fail(ResponseCode.CUSTOM_FAILED.getCode(), ce.getMsg());
        }
    }

    @OperationLogDetail(detail = "生成Google密钥", level = 3, operationUnit = OperationUnit.GOOGLE)
    @GetMapping("/ggs")
    @ResponseBody
    @NeedLogin
    public ResponseParent<GoogleSecretVO> generateGoogleSecret(){
        try {
            return ResponseParent.succeed(userService.generateGoogleSecret(this.getUser()));
        }catch (CustomException ce){
            return ResponseParent.fail(ResponseCode.AUTH_GOOGLE_NOT_FOUND.getCode(), ce.getMsg());
        }
    }

    @OperationLogDetail(detail = "生成二维码", level = 3, operationUnit = OperationUnit.GOOGLE)
    @GetMapping("/genQrCode")
    @ResponseBody
    public ResponseParent<?> genQrCode(String secretQrCode) {
        try {
            userService.genQrCode(secretQrCode, this.getResponse());
            return null;
        } catch (CustomException ce) {
            return ResponseParent.fail(ResponseCode.CUSTOM_FAILED.getCode(), ce.getMsg());
        }
    }

    @OperationLogDetail(detail = "绑定谷歌验证码", level = 3, operationUnit = OperationUnit.USER, operationType = OperationType.UPDATE)
    @PostMapping("/bg")
    @NeedLogin
    @ResponseBody
    public ResponseParent<String> bindGoogle(@RequestBody GoogleDTO dto){
        try {
            userService.bindGoogle(dto, this.getUser(), this.getRequest());
            return ResponseParent.succeed("绑定成功");
        } catch (CustomException ce) {
            return ResponseParent.fail(ResponseCode.CUSTOM_FAILED.getCode(), ce.getMsg());
        }
    }

    @OperationLogDetail(detail = "谷歌二次验证", level = 3, operationUnit = OperationUnit.GOOGLE)
    @PostMapping("/gl")
    @NeedLogin
    @ResponseBody
    public ResponseParent<String> googleLogin(@RequestBody GoogleLoginDTO dto){
        try {
            userService.googleLogin(dto.getCode(), this.getUser(), this.getRequest());
            return ResponseParent.succeed("Google验证成功");
        } catch (CustomException ce) {
            return ResponseParent.fail(ResponseCode.CUSTOM_FAILED.getCode(), ce.getMsg());
        }
    }
}
