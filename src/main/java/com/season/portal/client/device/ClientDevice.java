package com.season.portal.client.device;

import com.season.portal.PortalApplication;
import com.season.portal.client.generated.device.*;
import com.season.portal.client.generated.reseller.GetResellerByIdRequest;
import com.season.portal.client.generated.reseller.GetResellerByIdResponse;
import com.season.portal.devices.DeviceListPageModel;
import com.season.portal.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;

public class ClientDevice extends WebServiceGatewaySupport{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public GetCountDevicesFilteredResponse countDeviceFiltered(DeviceListPageModel model){
        String resellerId = model.getResellerId();
        String deviceId = model.getDeviceId();
        String status=model.getStatus();

        String startCreationDate = Utils.strToStrDateTime(model.getStartCreationDate());
        String endCreationDate = Utils.strToStrDateTime(model.getEndCreationDate());
        String startActivationDate = Utils.strToStrDateTime(model.getStartActivationDate());
        String endActivationDate = Utils.strToStrDateTime(model.getEndActivationDate());
        String startExpirationDate = Utils.strToStrDateTime(model.getStartExpirationDate());
        String endExpirationDate = Utils.strToStrDateTime(model.getEndExpirationDate());

        resellerId = (resellerId == null)?"":resellerId;
        deviceId = (deviceId == null)?"":deviceId;
        status = (status == null)?"":status;

        return countDeviceFiltered(
            resellerId,
            deviceId,
            status,
            startCreationDate,
            endCreationDate,
            startActivationDate,
            endActivationDate,
            startExpirationDate,
            endExpirationDate
        );
    }
    public GetCountDevicesFilteredResponse countDeviceFiltered(String resellerId, String deviceId, String status,
                                                               String startCreationDate, String endCreationDate,
                                                               String startActivationDate, String endActivationDate,
                                                               String startExpirationDate, String endExpirationDate){
        GetCountDevicesFilteredRequest request = new GetCountDevicesFilteredRequest();
        request.setResellerId(resellerId);
        request.setDeviceId(deviceId);
        request.setStatus(status);
        request.setStartCreationDate(startCreationDate);
        request.setEndCreationDate(endCreationDate);
        request.setStartActivationDate(startActivationDate);
        request.setEndActivationDate(endActivationDate);
        request.setStartExpirationDate(startExpirationDate);
        request.setEndExpirationDate(endExpirationDate);

        GetCountDevicesFilteredResponse response = null;
        try {
            response = (GetCountDevicesFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientDevice_countDeviceFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientDevice_countDeviceFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.addErrorKey("api_ClientDevice_countDeviceFiltered_ex");
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

    public GetDevicesFilteredResponse getDeviceFiltered(DeviceListPageModel model){
        String resellerId = model.getResellerId();
        String deviceId = model.getDeviceId();
        String status=model.getStatus();

        String startCreationDate = Utils.strToStrDateTime(model.getStartCreationDate());
        String endCreationDate = Utils.strToStrDateTime(model.getEndCreationDate());
        String startActivationDate = Utils.strToStrDateTime(model.getStartActivationDate());
        String endActivationDate = Utils.strToStrDateTime(model.getEndActivationDate());
        String startExpirationDate = Utils.strToStrDateTime(model.getStartExpirationDate());
        String endExpirationDate = Utils.strToStrDateTime(model.getEndExpirationDate());

        resellerId = (resellerId == null)?"":resellerId;
        deviceId = (deviceId == null)?"":deviceId;
        status = (status == null)?"":status;

        return getDeviceFiltered(
                resellerId,
                deviceId,
                status,
                startCreationDate,
                endCreationDate,
                startActivationDate,
                endActivationDate,
                startExpirationDate,
                endExpirationDate,
                model.getValidOffset(),
                model.getValidNumPerPage(),
                model.getValidSort(),
                model.getValidOrder()
        );
    }
    public GetDevicesFilteredResponse getDeviceFiltered(String resellerId, String deviceId, String status,
                                                        String startCreationDate, String endCreationDate,
                                                        String startActivationDate, String endActivationDate,
                                                        String startExpirationDate, String endExpirationDate,
                                                        int offset, int numberRecords, String field, String order){
        GetDevicesFilteredRequest request = new GetDevicesFilteredRequest();
        request.setResellerId(resellerId);
        request.setDeviceId(deviceId);
        request.setStatus(status);
        request.setStartCreationDate(startCreationDate);
        request.setEndCreationDate(endCreationDate);
        request.setStartActivationDate(startActivationDate);
        request.setEndActivationDate(endActivationDate);
        request.setStartExpirationDate(startExpirationDate);
        request.setEndExpirationDate(endExpirationDate);

        request.setOffset(offset);
        request.setNumberRecords(numberRecords);
        request.setField(field);
        request.setOrderField(order);

        GetDevicesFilteredResponse response = null;
        try {
            response = (GetDevicesFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientDevice_getDeviceFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientDevice_getDeviceFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.addErrorKey("api_ClientDevice_getDeviceFiltered_ex");
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

    public GetDeviceByIdResponse getDeviceById(String deviceId) {
        GetDeviceByIdRequest request = new GetDeviceByIdRequest();
        request.setDeviceId(deviceId);

        GetDeviceByIdResponse response = null;
        try {
            response = (GetDeviceByIdResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientDevice_getDeviceById_noCode");
            else
                PortalApplication.addErrorKey("api_ClientDevice_getDeviceById_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.addErrorKey("api_ClientDevice_getDeviceById_ex");
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

}

