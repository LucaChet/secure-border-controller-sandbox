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
                private String allowedIPsString;  // Rappresenta la stringa originale degli indirizzi IP

                // Metodo per ottenere la lista di indirizzi IP
                public List<String> getAllowedIPs() {
                    // Dividi la stringa degli indirizzi IP usando la virgola e lo spazio come separatore
                    return Arrays.asList(allowedIPsString.split(", "));
                }

                // Getter e setter per la stringa degli allowedIPs
                public String getAllowedIPsString() {
                    return allowedIPsString;
                }

                public void setAllowedIPsString(String allowedIPsString) {
                    this.allowedIPsString = allowedIPsString;
                }
            }

            // Getter e setter per peerConfiguration
            public PeerConfiguration getPeerConfiguration() {
                return peerConfiguration;
            }

            public void setPeerConfiguration(PeerConfiguration peerConfiguration) {
                this.peerConfiguration = peerConfiguration;
            }
        }

        // Getter e setter per connection
        public Connection getConnection() {
            return connection;
        }

        public void setConnection(Connection connection) {
            this.connection = connection;
        }
    }

    // Getter e setter per apiVersion
    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    // Getter e setter per kind
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    // Getter e setter per metadata
    public V1ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(V1ObjectMeta metadata) {
        this.metadata = metadata;
    }

    // Getter e setter per status
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
