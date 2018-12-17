package com.example.mj975.woder_woman.data;

public class Report {
    private String address;
    private String detailAddress;
    private String content;
    private String uri;
    private String email;

    public Report() {
    }

    public Report(String address, String detailAddress, String content, String uri, String email) {
        this.address = address;
        this.detailAddress = detailAddress;
        this.content = content;
        this.uri = uri;
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
