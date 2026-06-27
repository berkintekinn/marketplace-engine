package com.marketplace.models;

public class UnauthorizedAccessException extends RuntimeException 
{
    public UnauthorizedAccessException(String message) 
    {
        super(message);
    }
}