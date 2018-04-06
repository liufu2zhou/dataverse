package edu.harvard.iq.dataverse.api;

import edu.harvard.iq.dataverse.DataverseSession;
import edu.harvard.iq.dataverse.DvObject;
import edu.harvard.iq.dataverse.DvObjectServiceBean;
import edu.harvard.iq.dataverse.feedback.Feedback;
import edu.harvard.iq.dataverse.feedback.FeedbackUtil;
import javax.ejb.EJB;
import javax.json.JsonObject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("admin/feedback")
public class FeedbackApi extends AbstractApiBean {

    @EJB
    DvObjectServiceBean dvObjectSvc;

    @POST
    public Response submitFeedback(JsonObject jsonObject) throws AddressException {
        DvObject recipient = dvObjectSvc.findDvObject(jsonObject.getJsonNumber("id").longValue());
        DataverseSession dataverseSession = null;
        String userMessage = jsonObject.getString("body");
        String systemEmail = "support@librascholar.edu";
        InternetAddress systemAddress = new InternetAddress(systemEmail);
        String userEmail = jsonObject.getString("fromEmail");
        String messageSubject = jsonObject.getString("subject");
        String baseUrl = systemConfig.getDataverseSiteUrl();
        Feedback feedback = FeedbackUtil.gatherFeedback(recipient, dataverseSession, messageSubject, userMessage, systemAddress, userEmail, baseUrl);
        return ok(feedback.toJsonObjectBuilder());
    }
}