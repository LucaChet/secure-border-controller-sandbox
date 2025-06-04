package eu.fluidos.Crds;

import com.google.gson.annotations.SerializedName;
import io.kubernetes.client.openapi.models.V1ObjectMeta;

import java.util.Arrays;
import java.util.List;

public class TunnelEndpoint {
    @SerializedName("apiVersion")
    private String apiVersion;

    @SerializedName("kind")
    private String kind;

    @SerializedName("metadata")
    private V1ObjectMeta metadata;

    @SerializedName("status")
    private Status status;

    public static class Status {
        @SerializedName("connection")
        private Connection connection;

        public static class Connection {
            @SerializedName("peerConfiguration")
            private PeerConfiguration peerConfiguration;

            public static class PeerConfiguration {
                @SerializedName("allowedIPs")
                private String allowedIPsString; 
                public List<String> getAllowedIPs() {
                    return Arrays.asList(allowedIPsString.split(", "));
                }

                public String getAllowedIPsString() {
                    return allowedIPsString;
                }

                public void setAllowedIPsString(String allowedIPsString) {
                    this.allowedIPsString = allowedIPsString;
                }
            }

            public PeerConfiguration getPeerConfiguration() {
                return peerConfiguration;
            }

            public void setPeerConfiguration(PeerConfiguration peerConfiguration) {
                this.peerConfiguration = peerConfiguration;
            }
        }

        public Connection getConnection() {
            return connection;
        }

        public void setConnection(Connection connection) {
            this.connection = connection;
        }
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public V1ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(V1ObjectMeta metadata) {
        this.metadata = metadata;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
