# Live
This is a backend project for fast real-time live-streaming that mimics the real applications under high concurency environment(with 10W+ living users per second). 

## Technical Choice
Netty for self-designed IM system and , 
RocketMQ for loose coulped microservices, 
MySQL for information storage(organised with Sharing-JDBC), 
Redis for user caching, 
Spring Cloud Alibaba Nacos for service discovery and distributed configuration management, 
Dubbo for load balancing and RPC request for Downstream services,
Docker for environment isolation and deployment.

## Project Modules


## Bussiness Logics
login to the frontpage -> view the live-streaming -> buys gifts/send gifts under high concurency environment -> exists to the frontpage.

# Architecture of Self-designed Instant Messaging System

# Deployment Architecture

# 

# Special Thanks
