package com.debayan.JournalApp.Api.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherResponse {

    public Current current;

   @Getter
   @Setter
   public static class Current {
        private int temperature;

        @JsonProperty("weather_descriptions")
        public List<String> weatherDescriptions;
        private int feelslike;;
    }

}