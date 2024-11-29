package com.andersen.java_intensive_13.hotel_app.utils;

import com.andersen.java_intensive_13.hotel_app.enums.UserRole;
import com.andersen.java_intensive_13.hotel_app.model.Apartment;
import com.andersen.java_intensive_13.hotel_app.model.User;

import java.util.ArrayList;
import java.util.List;

public class PrepatedEntity {
    public static List<Apartment> preparedApartmentList(){
        Apartment apartment1 = Apartment.builder()
                .id(1)
                .price(3000D)
                .isReserved(false)
                .user(null)
                .build();

        Apartment apartment2 = Apartment.builder()
                .id(2)
                .price(4000D)
                .isReserved(false)
                .user(null)
                .build();

        List<Apartment> apartmentList = new ArrayList<>();
        apartmentList.add(apartment1);
        apartmentList.add(apartment2);
        return apartmentList;
    }

    public static List<User> preparedUserList(){
        User user1 = User.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Grande")
                .userRole(UserRole.USER)
                .build();
        User user2 = User.builder()
                .id(2L)
                .firstName("Sam")
                .lastName("Smith")
                .userRole(UserRole.ADMIN)
                .build();
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        return userList;
    }
}
