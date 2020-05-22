/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Admin
 */
public class StoreHouse {
    private int STT;
    private String maSP;
    private String tenSanPham;
    private String donGiaSanPham;
    private String soLuong;
    private String size;
    private String loai;

    public StoreHouse() {
    }

    public StoreHouse(int STT, String maSP, String tenSanPham, String donGiaSanPham, String soLuong, String size, String loai) {
        this.STT = STT;
        this.maSP = maSP;
        this.tenSanPham = tenSanPham;
        this.donGiaSanPham = donGiaSanPham;
        this.soLuong = soLuong;
        this.size = size;
        this.loai = loai;
    }

    public int getSTT() {
        return STT;
    }

    public void setSTT(int STT) {
        this.STT = STT;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getDonGiaSanPham() {
        return donGiaSanPham;
    }

    public void setDonGiaSanPham(String donGiaSanPham) {
        this.donGiaSanPham = donGiaSanPham;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    
}

