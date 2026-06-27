package com.marketplace.dto;

import com.marketplace.models.Role;

public record RegisterRequest(String name, String email, String password, Role role) {}