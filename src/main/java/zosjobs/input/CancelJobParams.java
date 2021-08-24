package zosjobs.input;

import java.util.Optional;

public class CancelJobParams {
    private Optional<String> jobname;
    private Optional<String> jobId;
    private Optional<String> version;

    public CancelJobParams(CancelJobParams.Builder builder) {


        if (builder.jobname != null)
            this.jobname = Optional.ofNullable(builder.jobname);
        else this.jobname = Optional.empty();

        if (builder.jobId != null)
            this.jobId = Optional.ofNullable(builder.jobId);
        else this.jobId = Optional.empty();

        if (builder.version != null)
            this.version = Optional.ofNullable(builder.version);
        else this.version = Optional.empty();
    }


    public Optional<String> getJobname() {
        return jobname;
    }

    public Optional<String> getJobId() {
        return jobId;
    }

    public Optional<String> getVersion() { return version; }

    @Override
    public String toString() {
        return "CancelJobParams{" +
                "jobId=" + jobId +
                ", jobname=" + jobname +
                ", version=" + version +
                '}';
    }

    public static class Builder {

        private String jobname;
        private String jobId;
        private String version;

        public CancelJobParams.Builder jobname(String jobname) {
            this.jobname = jobname;
            return this;
        }

        public CancelJobParams.Builder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public CancelJobParams.Builder version(String version) {
            this.version = version;
            return this;
        }

        public CancelJobParams build() {
            return new CancelJobParams(this);
        }

    }
}
