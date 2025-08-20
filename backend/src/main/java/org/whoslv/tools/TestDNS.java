package org.whoslv.tools;

import java.net.InetAddress;

public class TestDNS {
    public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getByName("db.biearchwjxvxpetfuaxy.supabase.co");
            System.out.println("Endereço IP: " + address.getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
