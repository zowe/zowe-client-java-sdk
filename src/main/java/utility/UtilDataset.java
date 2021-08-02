package utility;

import org.json.simple.JSONObject;
import rest.Response;
import zosfiles.response.Dataset;

public class UtilDataset {

    public static Dataset createDatasetObjFromJson(JSONObject json) {
        return new Dataset.Builder().dsname((String) json.get("dsname"))
                .blksz((String) json.get("blksz"))
                .catnm((String) json.get("catnm"))
                .cdate((String) json.get("cdate"))
                .dev((String) json.get("dev"))
                .dsntp((String) json.get("dsntp"))
                .dsorg((String) json.get("dsorg"))
                .edate((String) json.get("edate"))
                .extx((String) json.get("extx"))
                .lrectl((String) json.get("lrectl"))
                .migr((String) json.get("migr"))
                .mvol((String) json.get("mvol"))
                .ovf((String) json.get("ovf"))
                .rdate((String) json.get("rdate"))
                .recfm((String) json.get("recfm"))
                .sizex((String) json.get("sizex"))
                .spacu((String) json.get("spacu"))
                .used((String) json.get("used"))
                .vol((String) json.get("vol"))
                .build();
    }

    public static void checkHttpErrors(Response response, String dataSetName) throws Exception {
        int httpCode = response.getStatusCode().get();
        if (response.getResponsePhrase().isPresent() && Util.isHttpError(httpCode)) {
            String responsePhrase = (String) response.getResponsePhrase().get();
            String errorMsg = httpCode + " " + responsePhrase + ".";
            if (httpCode == 404) {
                throw new Exception(errorMsg + " You may have specified an invalid or non-existent data set.");
            }
            if (httpCode == 500) {
                throw new Exception(errorMsg + " You may not have permission to view " + dataSetName + ".");
            }
            throw new Exception(errorMsg);
        }
    }


}
