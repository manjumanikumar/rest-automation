package com.fastspringapi.util;

import com.fasspringapi.server.ProductApiServer;

public class BaseClass {
        protected ProductApiServer server = new ProductApiServer("https", "qa-api.fastspring.com", 443);
}
