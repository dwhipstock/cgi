package com.cgi;

import com.cgi.model.NumberArrayResponse;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.IntStream;

public class CodeTestApplication {

    public static void main(String[] args) throws IOException {
        if (args[0] == null) {
            System.out.println("Please pass in the url to be called.");
            return;
        }
        new CodeTestApplication(args[0]);
    }

    public CodeTestApplication(String urlAsString) {
        HttpURLConnection httpURLConnection = null;
        String responseAsString = null;

        try {
            httpURLConnection = doRequest(urlAsString);
            responseAsString = getGetResponseAsString(httpURLConnection);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        NumberArrayResponse numberArrayResponse[] = new Gson()
                .fromJson(responseAsString, NumberArrayResponse[].class);

        int runningSum = displayRunningSumsAndGetRunningSum(numberArrayResponse);
        System.out.println(runningSum);
    }

    //Displays a sum for each array in NumberArrayResponse[] returns a sum of all of the array sums.
    private int displayRunningSumsAndGetRunningSum(NumberArrayResponse[] numberArrayResponse) {
        int runningSum = 0;

        for (NumberArrayResponse numberArray : numberArrayResponse) {
            int sum = IntStream.of(numberArray.getNumbers()).sum();
            System.out.println(sum);
            runningSum += sum;
        }
        return runningSum;
    }

    //Does a GET request to the given url as String and passes back the HttpURLConnection
    private HttpURLConnection doRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        return httpURLConnection;
    }

    //Takes in a HttpURLConnection object and returns the response content and a string
    private String getGetResponseAsString(HttpURLConnection httpURLConnection) throws IOException {
        StringBuffer response = new StringBuffer();
        String inputLine;

        //Try with resources - close with finally not needed.
        try (BufferedReader bufferedReader =
                     new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }
}
