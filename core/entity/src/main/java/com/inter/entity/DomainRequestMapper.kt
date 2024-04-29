package com.inter.entity

interface DomainRequestMapper<DOMAIN, REQUEST> {
    fun toRequest(domain: DOMAIN): REQUEST
}