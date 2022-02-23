package com.season.portal.client.users;

import com.season.portal.PortalApplication;
import com.season.portal.client.generated.user.*;
import com.season.portal.users.UsersListPageModel;
import com.season.portal.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import java.time.LocalDateTime;
import java.util.Date;

public class ClientUser extends WebServiceGatewaySupport{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public enum USER_STATUS {
        CHANGEPW,
        BLOCKED,
        INACTIVE,
        ACTIVE
    }
    public ChangeUserPwResponse changePass(String newPassUserId, String pass, String actionUserId) {
        ChangeUserPwRequest request = new ChangeUserPwRequest();
        request.setPassword(pass);
        request.setUserId(newPassUserId);
        request.setActionUserId(actionUserId);

        ChangeUserPwResponse response = null;
        try {
            response = (ChangeUserPwResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientUser_changePass_noCode");
            else
                PortalApplication.addErrorKey("api_ClientUser_changePass_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_changePass_ex");
        }

        return response;
    }

    public boolean validateChangeUserPwResponse(ChangeUserPwResponse response, boolean addMsg) {
        boolean valid = false;

        if(response != null){
            if(response.isResult()){
                valid = true;
                if(addMsg)
                    PortalApplication.addSuccessKey("api_ClientUser_validateChangeUserPwResponse_success");
            }
            else if(addMsg){
                PortalApplication.addErrorKey("api_ClientUser_validateChangeUserPwResponse_error");
            }
        }

        return valid;
    }

    public UserLoginResponse tryLogin(String email, String pass) {
        UserLoginRequest request = new UserLoginRequest();
        request.setUserEmail(email);
        request.setUserPassword(pass);

        UserLoginResponse response = null;
        try {
            response = (UserLoginResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals("")){
                PortalApplication.addErrorKey("api_ClientUser_tryLogin_noCode");
            }
            else{
                PortalApplication.addErrorKey("api_ClientUser_tryLogin_"+code);
            }

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_tryLogin_ex");
        }

        return response;
    }

    public GetUserRolesByUserIdResponse getRolesById(String userId) {
        GetUserRolesByUserIdRequest request = new GetUserRolesByUserIdRequest();
        request.setUserId(userId);

        GetUserRolesByUserIdResponse response = null;
        try {
            response = (GetUserRolesByUserIdResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals("")){
                PortalApplication.addErrorKey("api_ClientUser_getRolesById_noCode");
            }
            else{
                PortalApplication.addErrorKey("api_ClientUser_getRolesById_"+code);
            }

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_getRolesById_ex");
        }

        return response;
    }

    public GetUserByIdResponse getUserById(String userId) {
        GetUserByIdRequest request = new GetUserByIdRequest();
        request.setUserId(userId);

        GetUserByIdResponse response = null;
        try {
            response = (GetUserByIdResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientUser_getUserById_noCode");
            else
                PortalApplication.addErrorKey("api_ClientUser_getUserById_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_getUserById_ex");
        }

        return response;
    }

    public ExistUserEmailResponse existUserEmail(String email) {
        ExistUserEmailRequest request = new ExistUserEmailRequest();
        request.setUserEmail(email);

        ExistUserEmailResponse response = null;
        try {
            response = (ExistUserEmailResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientUser_existUserEmail_noCode");
            else
                PortalApplication.addErrorKey("api_ClientUser_existUserEmail_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_existUserEmail_ex");
        }

        return response;
    }

    public ExistUserNameResponse existUserName(String name) {
        ExistUserNameRequest request = new ExistUserNameRequest();
        request.setUserName(name);

        ExistUserNameResponse response = null;
        try {
            response = (ExistUserNameResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientUser_existUserName_noCode");
            else
                PortalApplication.addErrorKey("api_ClientUser_existUserName_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_existUserName_ex");
        }

        return response;
    }

    public UnblockUserResponse unblockUser(String userId, String actionUserId) {
        UnblockUserRequest request = new UnblockUserRequest();
        request.setUserId(userId);
        request.setActionUserId(actionUserId);

        UnblockUserResponse response = null;
        try {
            response = (UnblockUserResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientUser_unblockUser_noCode");
            else
                PortalApplication.addErrorKey("api_ClientUser_unblockUser_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_unblockUser_ex");
        }

        return response;
    }

    public boolean validateUnblockUserResponse(UnblockUserResponse response, boolean addMsg) {
        boolean valid = false;

        if(response != null){
            if(response.isResult()){
                valid = true;
                if(addMsg)
                    PortalApplication.addSuccessKey("api_ClientUser_validateUnblockUserResponse_success");
            }
            else if(addMsg){
                PortalApplication.addErrorKey("api_ClientUser_validateUnblockUserResponse_error");
            }
        }

        return valid;
    }

    public UnblockUserResponse activateUser(String userId, String actionUserId) {
        PortalApplication.addErrorKey("api_notImplementedYet");
        return null;
        /*UnblockUserRequest request = new UnblockUserRequest();
        request.setUserId(userId);
        request.setActionUserId(actionUserId);

        UnblockUserResponse response = null;
        try {
            response = (UnblockUserResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientUser_activateUser_noCode");
            else
                PortalApplication.addErrorKey("api_ClientUser_activateUser_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_activateUser_ex");
        }

        return response;*/
    }

    public boolean validateActivateResponse(UnblockUserResponse response, boolean addMsg) {
        boolean valid = false;

        if(response != null){
            if(response.isResult()){
                valid = true;
                if(addMsg)
                    PortalApplication.addSuccessKey("api_ClientUser_validateActivateUserResponse_success");
            }
            else if(addMsg){
                PortalApplication.addErrorKey("api_ClientUser_validateActivateUserResponse_error");
            }
        }

        return valid;
    }

    public InactivateUserResponse inactivateUser(String userId, String actionUserId) {
        InactivateUserRequest request = new InactivateUserRequest();
        request.setUserId(userId);
        request.setActionUserId(actionUserId);

        InactivateUserResponse response = null;
        try {
            response = (InactivateUserResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientUser_inactivateUser_noCode");
            else
                PortalApplication.addErrorKey("api_ClientUser_inactivateUser_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_inactivateUser_ex");
        }

        return response;
    }

    public boolean validateInactivateUserResponse(InactivateUserResponse response, boolean addMsg) {
        boolean valid = false;

        if(response != null){
            if(response.isResult()){
                valid = true;
                if(addMsg)
                    PortalApplication.addSuccessKey("api_ClientUser_validateInactivateUserResponse_success");
            }
            else if(addMsg){
                PortalApplication.addErrorKey("api_ClientUser_validateInactivateUserResponse_error");
            }
        }

        return valid;
    }


    public SetUserResponse setUser(String email, String nickname, String pass, String actionUserId){
        SetUserRequest request = new SetUserRequest();
        request.setUserEmail(email);
        request.setUserName(nickname);
        request.setUserPassword(pass);
        request.setActionUserId(actionUserId);

        SetUserResponse response = null;
        try {
            response = (SetUserResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientUser_setUser_noCode");
            else
                PortalApplication.addErrorKey("api_ClientUser_setUser_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_setUser_ex");
        }

        return response;
    }


    public GetCountUserFilteredResponse countUserFiltered(UsersListPageModel model){
        String email = model.getEmail();
        String nickname=model.getNickName();
        String status=model.getStatus();
        String startDate= Utils.strToStrDate(model.getStartDate());
        String endDate= Utils.strToStrDate(model.getEndDate());

        email = (email == null)?"":email;
        nickname = (nickname == null)?"":nickname;
        status = (status == null)?"":status;

        return countUserFiltered(
                "",
                email,
                nickname,
                status,
                startDate,
                endDate
        );
    }
    public GetCountUserFilteredResponse countUserFiltered(String userId, String email, String nickname, String status, String startDate, String endDate){
        GetCountUserFilteredRequest request = new GetCountUserFilteredRequest();
        request.setUserId(userId);
        request.setUserEmail(email);
        request.setUserName(nickname);
        request.setStartCreationDate(startDate);
        request.setEndCreationDate(endDate);
        request.setUserStatus(status);

        GetCountUserFilteredResponse response = null;
        try {
            response = (GetCountUserFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientUser_countUserFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientUser_countUserFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_countUserFiltered_ex");
        }

        return response;
    }

    public GetUserFilteredResponse getUserFiltered(UsersListPageModel model){
        String email = model.getEmail();
        String nickname=model.getNickName();
        String status=model.getStatus();
        String startDate= Utils.strToStrDate(model.getStartDate());
        String endDate= Utils.strToStrDate(model.getEndDate());

        email = (email == null)?"":email;
        nickname = (nickname == null)?"":nickname;
        status = (status == null)?"":status;

        return getUserFiltered(
                "",
                email,
                nickname,
                status,
                startDate,
                endDate,
                model.getValidOffset(),
                model.getValidNumPerPage(),
                model.getValidSort(),
                model.getValidOrder()
        );
    }
    public GetUserFilteredResponse getUserFiltered(String userId, String email, String nickname, String status, String startDate, String endDate,
                                                   int offset, int numberRecords, String field, String order){
        GetUserFilteredRequest request = new GetUserFilteredRequest();
        request.setUserId(userId);
        request.setUserEmail(email);
        request.setUserName(nickname);
        request.setStartCreationDate(startDate);
        request.setEndCreationDate(endDate);
        request.setUserStatus(status);
        request.setOffset(offset);
        request.setNumberRecords(numberRecords);
        request.setField(field);
        request.setOrderField(order);

        GetUserFilteredResponse response = null;
        try {
            response = (GetUserFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientUser_getUserFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientUser_getUserFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_getUserFiltered_ex");
        }

        return response;
    }
}

