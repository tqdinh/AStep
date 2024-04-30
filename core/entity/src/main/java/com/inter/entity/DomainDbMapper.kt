package com.inter.entity

interface DomainDbMapper<DOMAIN, DATABASE> {
    fun toDomain(db: DATABASE): DOMAIN
    fun toDatabase(domain: DOMAIN): DATABASE

}