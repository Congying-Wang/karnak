package org.karnak.data.gateway;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.group.GroupSequenceProvider;
import org.karnak.data.gateway.DestinationGroupSequenceProvider.DestinationDicomGroup;
import org.karnak.data.gateway.DestinationGroupSequenceProvider.DestinationStowGroup;

@GroupSequenceProvider(value = DestinationGroupSequenceProvider.class)
@Entity(name = "Destination")
@Table(name = "destination")
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    @NotNull(message = "Type is mandatory")
    private DestinationType type;

    private boolean desidentification;

    // list of emails (comma separated) used when the images have been sent (or
    // partially sent) to the final destination. Note: if an issue appears before
    // sending to the final destination then no email is delivered.
    private String notify;

    // Prefix of the email object when containing an issue. Default value: **ERROR**
    private String notifyObjectErrorPrefix;

    // Pattern of the email object, see https://dzone.com/articles/java-string-format-examples. Default value:
    // [Karnak Notification] %s %.30s
    private String notifyObjectPattern;

    // Values injected in the pattern [PatientID StudyDescription StudyDate
    // StudyInstanceUID]. Default value: PatientID,StudyDescription
    private String notifyObjectValues;

    // Interval in seconds for sending a notification (when no new image is arrived
    // in the archive folder). Default value: 45
    private Integer notifyInterval;

    // DICOM properties

    // the AETitle of the destination node.
    // mandatory[type=dicom]
    @NotBlank(groups = DestinationDicomGroup.class, message = "AETitle is mandatory")
    @Size(groups = DestinationDicomGroup.class, max = 16, message = "AETitle has more than 16 characters")
    private String aeTitle;

    // the host or IP of the destination node.
    // mandatory[type=dicom]
    @NotBlank(groups = DestinationDicomGroup.class, message = "Hostname is mandatory")
    private String hostname;

    // the port of the destination node.
    // mandatory[type=dicom]
    @NotNull(groups = DestinationDicomGroup.class, message = "Port is mandatory")
    @Min(groups = DestinationDicomGroup.class, value = 1, message = "Port should be between 1 and 65535")
    @Max(groups = DestinationDicomGroup.class, value = 65535, message = "Port should be between 1 and 65535")
    private Integer port;

    // false by default; if "true" then use the destination AETitle as the calling
    // AETitle at the gateway side. Otherwise with "false" the calling AETitle is
    // the AETitle defined in the property "listener.aet" of the file
    // gateway.properties.
    private Boolean useaetdest;

    // STOW properties

    // the destination STOW-RS URL.
    // mandatory[type=stow]
    @NotBlank(groups = DestinationStowGroup.class, message = "URL is mandatory")
    private String url;

    // credentials of the STOW-RS service (format is "user:password").
    private String urlCredentials;

    // headers for HTTP request.
    @Size(max = 4096, message = "Headers has more than 4096 characters")
    private String headers;

    @ManyToOne
    @JoinColumn
    private ForwardNode forwardNode;

    public static Destination ofDicomEmpty() {
        return new Destination(DestinationType.dicom);
    }

    public static Destination ofDicom(String description, String aeTitle, String hostname, int port,
        Boolean useaetdest) {
        Destination destination = new Destination(DestinationType.dicom);
        destination.setDescription(description);
        destination.setAeTitle(aeTitle);
        destination.setHostname(hostname);
        destination.setPort(Integer.valueOf(port));
        destination.setUseaetdest(useaetdest);
        return destination;
    }

    public static Destination ofStowEmpty() {
        return new Destination(DestinationType.stow);
    }

    public static Destination ofStow(String description, String url, String urlCredentials, String headers) {
        Destination destination = new Destination(DestinationType.stow);
        destination.setDescription(description);
        destination.setUrl(url);
        destination.setUrlCredentials(urlCredentials);
        destination.setHeaders(headers);
        return destination;
    }

    protected Destination() {
        this.type = null;
        this.description = "";
        this.desidentification = true;
        this.notify = "";
        this.notifyObjectErrorPrefix = "";
        this.notifyObjectPattern = "";
        this.notifyObjectValues = "";
        this.notifyInterval = Integer.valueOf(0);
        this.aeTitle = "";
        this.hostname = "";
        this.port = Integer.valueOf(0);
        this.useaetdest = Boolean.FALSE;
        this.url = "";
        this.urlCredentials = "";
        this.headers = "";
    }

    protected Destination(DestinationType type) {
        this();
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public boolean isNewData() {
        return id == null;
    }

    public String getStringReference() {
        if (type != null) {
            switch (type) {
                case dicom:
                    return getAeTitle();
                case stow:
                    return getUrl() + ":" + getPort();
            }
        }
        return "Type of destination is unknown";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DestinationType getType() {
        return type;
    }

    public boolean getDesidentification() {
        return desidentification;
    }

    public void setDesidentification(boolean desidentification) {
        this.desidentification = desidentification;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getNotifyObjectErrorPrefix() {
        return notifyObjectErrorPrefix;
    }

    public void setNotifyObjectErrorPrefix(String notifyObjectErrorPrefix) {
        this.notifyObjectErrorPrefix = notifyObjectErrorPrefix;
    }

    public String getNotifyObjectPattern() {
        return notifyObjectPattern;
    }

    public void setNotifyObjectPattern(String notifyObjectPattern) {
        this.notifyObjectPattern = notifyObjectPattern;
    }

    public String getNotifyObjectValues() {
        return notifyObjectValues;
    }

    public void setNotifyObjectValues(String notifyObjectValues) {
        this.notifyObjectValues = notifyObjectValues;
    }

    public Integer getNotifyInterval() {
        return notifyInterval;
    }

    public void setNotifyInterval(Integer notifyInterval) {
        this.notifyInterval = notifyInterval;
    }

    public String getAeTitle() {
        return aeTitle;
    }

    public void setAeTitle(String aeTitle) {
        this.aeTitle = aeTitle;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getUseaetdest() {
        return useaetdest;
    }

    public void setUseaetdest(Boolean useaetdest) {
        this.useaetdest = useaetdest;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlCredentials() {
        return urlCredentials;
    }

    public void setUrlCredentials(String urlCredentials) {
        this.urlCredentials = urlCredentials;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public ForwardNode getForwardNode() {
        return forwardNode;
    }

    public void setForwardNode(ForwardNode forwardNode) {
        this.forwardNode = forwardNode;
    }

    /**
     * Informs if this object matches with the filter as text.
     * 
     * @param filterText
     *            the filter as text.
     * @return true if this object matches with the filter as text; false otherwise.
     */
    public boolean matchesFilter(String filterText) {
        if (contains(description, filterText) //
            || contains(notify, filterText) //
            || contains(notifyObjectErrorPrefix, filterText) //
            || contains(notifyObjectPattern, filterText) //
            || contains(notifyObjectValues, filterText) //
            || contains(aeTitle, filterText) //
            || contains(hostname, filterText) //
            || contains(aeTitle, filterText) //
            || equals(port, filterText) //
            || contains(url, filterText) //
            || contains(urlCredentials, filterText) //
            || contains(headers, filterText)) {
            return true;
        }
        return false;
    }

    private boolean contains(String value, String filterText) {
        return value != null && value.contains(filterText);
    }

    private boolean equals(Integer value, String filterText) {
        return value != null && value.toString().equals(filterText);
    }

    @Override
    public String toString() {
        if (type != null) {
            switch (type) {
                case dicom:
                    return "Destination [id=" + id + ", description=" + description + ", type=" + type + ", notify="
                        + notify + ", notifyObjectErrorPrefix=" + notifyObjectErrorPrefix + ", notifyObjectPattern="
                        + notifyObjectPattern + ", notifyObjectValues=" + notifyObjectValues + ", notifyInterval="
                        + notifyInterval + ", aeTitle=" + aeTitle + ", hostname=" + hostname + ", port=" + port
                        + ", useaetdest=" + useaetdest + "]";
                case stow:
                    return "Destination [id=" + id + ", description=" + description + ", type=" + type + ", notify="
                        + notify + ", notifyObjectErrorPrefix=" + notifyObjectErrorPrefix + ", notifyObjectPattern="
                        + notifyObjectPattern + ", notifyObjectValues=" + notifyObjectValues + ", notifyInterval="
                        + notifyInterval + ", url=" + url + ", urlCredentials=" + urlCredentials + ", headers="
                        + headers + "]";
            }
        }
        return "Destination [id=" + id + ", description=" + description + ", type=" + type + ", notify=" + notify
            + ", notifyObjectErrorPrefix=" + notifyObjectErrorPrefix + ", notifyObjectPattern=" + notifyObjectPattern
            + ", notifyObjectValues=" + notifyObjectValues + ", notifyInterval=" + notifyInterval + "]";
    }
}
