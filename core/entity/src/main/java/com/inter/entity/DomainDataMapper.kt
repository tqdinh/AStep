package com.inter.entity

interface DomainDataMapper<DOMAIN, DATA> {
    fun toDomain(data: DATA): DOMAIN
    fun toData(domain: DOMAIN): DATA
}