/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

public class SanPham {
    private String maSP;
    private String tenSP;
    private int size;
    private int donGia;
    private int soLuong;
    private String loaiSP;

    public SanPham() {
    }

    public SanPham(String maSP, int soLuong, int size) {
        this.maSP = maSP;
        this.size = size;
        this.soLuong = soLuong;
    }

    public SanPham(String maSP, String tenSP, int donGia, String loaiSP) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donGia = donGia;
        this.loaiSP = loaiSP;
    }

    public SanPham(String maSP, String tenSP, int size, int donGia, int soLuong) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.size = size;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getLoaiSP() {
        return loaiSP;
    }

    public void setLoaiSP(String loaiSP) {
        this.loaiSP = loaiSP;
    }
    
}
