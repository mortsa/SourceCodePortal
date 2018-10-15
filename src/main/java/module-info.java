module cantara.docsite {
    requires jdk.unsupported;
    requires java.base;
    requires java.logging;
    requires java.management;
    requires java.xml.bind;
    requires com.sun.xml.fastinfoset;
    requires com.sun.xml.bind;
    requires java.net.http;
    requires java.json;
    requires java.json.bind;
    requires cache.api;
    requires org.jsoup;
    requires org.slf4j;
    requires jul_to_slf4j;
    requires log4j;
    requires commons.configuration;
    requires commons.logging;
    requires org.jboss.logging;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires hystrix.core;
    requires undertow.core;
    requires thymeleaf;
    requires thymeleaf.layout.dialect;
    requires thymeleaf.expression.processor;
    requires org.codehaus.groovy;
    requires org.commonmark;
    requires org.commonmark.ext.gfm.tables;
    requires asciidoctorj;
    requires no.ssb.config;

    opens config;
    opens no.cantara.docsite.domain.maven;

    exports no.cantara.docsite;
    exports no.cantara.docsite.controller;
    exports no.cantara.docsite.executor;
    exports no.cantara.docsite.domain.config;
    exports no.cantara.docsite.domain.github.commits;
    exports no.cantara.docsite.domain.github.contents;
    exports no.cantara.docsite.domain.github.pages;
    exports no.cantara.docsite.domain.github.releases;
    exports no.cantara.docsite.domain.github.repos;
    exports no.cantara.docsite.domain.github.webhook;
    exports no.cantara.docsite.util;
    exports no.cantara.docsite.web;
}
