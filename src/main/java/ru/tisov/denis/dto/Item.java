
package ru.tisov.denis.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "_id",
    "images",
    "address",
    "title",
    "price",
    "rooms",
    "beds",
    "area",
    "score",
    "partOfGroup",
    "groupId"
})
public class Item {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("images")
    private List<Image> images = null;
    @JsonProperty("address")
    private Address address;
    @JsonProperty("title")
    private Title title;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("rooms")
    private Integer rooms;
    @JsonProperty("beds")
    private Integer beds;
    @JsonProperty("area")
    private Integer area;
    @JsonProperty("score")
    private Integer score;
    @JsonProperty("partOfGroup")
    private Boolean partOfGroup;
    @JsonProperty("groupId")
    private String groupId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("images")
    public List<Image> getImages() {
        return images;
    }

    @JsonProperty("images")
    public void setImages(List<Image> images) {
        this.images = images;
    }

    @JsonProperty("address")
    public Address getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(Address address) {
        this.address = address;
    }

    @JsonProperty("title")
    public Title getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(Title title) {
        this.title = title;
    }

    @JsonProperty("price")
    public Integer getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Integer price) {
        this.price = price;
    }

    @JsonProperty("rooms")
    public Integer getRooms() {
        return rooms;
    }

    @JsonProperty("rooms")
    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    @JsonProperty("beds")
    public Integer getBeds() {
        return beds;
    }

    @JsonProperty("beds")
    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    @JsonProperty("area")
    public Integer getArea() {
        return area;
    }

    @JsonProperty("area")
    public void setArea(Integer area) {
        this.area = area;
    }

    @JsonProperty("score")
    public Integer getScore() {
        return score;
    }

    @JsonProperty("score")
    public void setScore(Integer score) {
        this.score = score;
    }

    @JsonProperty("partOfGroup")
    public Boolean getPartOfGroup() {
        return partOfGroup;
    }

    @JsonProperty("partOfGroup")
    public void setPartOfGroup(Boolean partOfGroup) {
        this.partOfGroup = partOfGroup;
    }

    @JsonProperty("groupId")
    public String getGroupId() {
        return groupId;
    }

    @JsonProperty("groupId")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("images", images).append("address", address).append("title", title).append("price", price).append("rooms", rooms).append("beds", beds).append("area", area).append("score", score).append("partOfGroup", partOfGroup).append("groupId", groupId).append("additionalProperties", additionalProperties).toString();
    }

}
