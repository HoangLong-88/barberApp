package com.example.babershop.controller

import com.example.babershop.model.BarberShop

class HomeController {
    val featuredShops = listOf(
        BarberShop("1", "King Barber Shop", "123 Street, HCM", "4.8", ""),
        BarberShop("2", "Elite Cuts Studio", "456 Avenue, HN", "4.5", ""),
        BarberShop("3", "Classic Barber", "789 Road, DN", "4.7", "")
    )
}