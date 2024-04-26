package com.inter.entity

interface Mapper<DOMAIN, DATA> {
    fun toDomain(data: DATA): DOMAIN
    fun toData(domain: DOMAIN): DATA
}