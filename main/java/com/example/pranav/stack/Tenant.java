package com.example.pranav.stack;

/**
 * Created by pranav on 03/10/15.
 */
public class Tenant {
    private String tenantId;
    private String tenantName;
    private String total_ram;
    private String tenantAvailabilityZone;
    private String total_instances;
    private String total_vcpus;

    public Tenant(){
        tenantId = " ";
        tenantName = " ";
        tenantAvailabilityZone = " ";
        total_ram = " ";
        total_instances = " ";
        total_vcpus = " ";
    }
    public String getTotal_ram() {
        return total_ram;
    }

    public void setTotal_ram(String total_ram) {
        this.total_ram = total_ram;
    }

    public String getTotal_vcpus() {
        return total_vcpus;
    }

    public void setTotal_vcpus(String total_vcpus) {
        this.total_vcpus = total_vcpus;
    }

    public String getTotal_instances() {
        return total_instances;
    }

    public void setTotal_instances(String total_instances) {
        this.total_instances = total_instances;
    }

    public String getTenantAvailabilityZone() {
        return tenantAvailabilityZone;
    }

    public void setTenantAvailabilityZone(String tenantAvailabilityZone) {
        this.tenantAvailabilityZone = tenantAvailabilityZone;
    }


    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
}
