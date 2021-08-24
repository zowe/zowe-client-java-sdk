package zosjobs.input;

import java.util.Optional;

public class DeleteJobParams {

    private Optional<String> jobname;
    private Optional<String> jobId;
    private Optional<String> modifyVersion;

    public DeleteJobParams(DeleteJobParams.Builder builder) {


        if (builder.jobname != null)
            this.jobname = Optional.ofNullable(builder.jobname);
        else this.jobname = Optional.empty();

        if (builder.jobId != null)
            this.jobId = Optional.ofNullable(builder.jobId);
        else this.jobId = Optional.empty();

        if (builder.modifyVersion != null)
            this.modifyVersion = Optional.ofNullable(builder.modifyVersion);
        else this.modifyVersion = Optional.empty();
    }


    public Optional<String> getJobname() {
        return jobname;
    }

    public Optional<String> getJobId() {
        return jobId;
    }

    public Optional<String> getModifyVersion() { return modifyVersion; }

    @Override
    public String toString() {
        return "DeleteJobParms{" +
                "jobId=" + jobId +
                ", jobname=" + jobname +
                ", modifyVersion=" + modifyVersion +
                '}';
    }

    public static class Builder {

        private String jobname;
        private String jobId;
        private String modifyVersion;

        public DeleteJobParams.Builder jobname(String jobname) {
            this.jobname = jobname;
            return this;
        }

        public DeleteJobParams.Builder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public DeleteJobParams.Builder modifyVersion(String modifyVersion) {
            this.modifyVersion = modifyVersion;
            return this;
        }

        public DeleteJobParams build() {
            return new DeleteJobParams(this);
        }

    }

}
