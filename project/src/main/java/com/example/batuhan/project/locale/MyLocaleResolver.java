package com.example.batuhan.project.locale;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@Component
public class MyLocaleResolver implements LocaleResolver{

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String language = request.getHeader("Accept-language");
		if(language == null || language.isEmpty()) {
			var x = Locale.forLanguageTag("tr");
			return x;
		}
		
		Locale locale = Locale.forLanguageTag(language);
		if(LanguageConfig.LOCALES.contains(locale)) {
			return locale;
		}
		return Locale.forLanguageTag("tr");
	}

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException("Cannot change locale - use a different locale resolution strategy");
    }



}
