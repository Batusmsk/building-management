package com.example.batuhan.project.locale;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Configuration;

@Configuration
public class LanguageConfig {
	public static final List<Locale> LOCALES = Arrays.asList(
			new Locale("tr")
			);
}
