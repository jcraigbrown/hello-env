package com.gr.tech;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class HelloEnv {

    public static final String ENV = "com.gr.tech.env";
    public static final String PERIOD = "com.gr.tech.periodMs";
    public static final String LOG_FILE = "com.gr.tech.logFile";

    private final String environment;
    private final long period;
    private final FileWriter logFileWriter;

    public HelloEnv(String env, long period, FileWriter logFileWriter) {
        this.environment = env;
        this.period = period;
        this.logFileWriter = logFileWriter;
    }

    public void logMessages() {
        while (true) {
            logMessage();
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void logMessage() {
        try {
            logFileWriter.append(toString());
            logFileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public String toString() {
        return "Hello " + environment + "!\n";
    }

    public static void main(String[] args) {
        try {
            String env = loadEnvironment();
            long period = loadPeriod();
            FileWriter logFileWriter = openLogFileWriter();

            HelloEnv helloEnv = new HelloEnv(env, period, logFileWriter);

            helloEnv.logMessages();

        } catch (Exception e) {
            e.printStackTrace(System.err);
            displayUsage();
        }
    }

    private static void displayUsage() {
        StringBuilder buf = new StringBuilder();

        buf.append("Usage: java");
        buf.append(" -D").append(ENV).append("=dev");
        buf.append(" -D").append(PERIOD).append("=10000");
        buf.append(" -D").append(LOG_FILE).append("=/tmp/hello-env.log");
        buf.append(" -jar hello-env.jar");

        System.out.println(buf.toString());
    }

    private static String loadEnvironment() {
        String env = getValue(ENV);

        if (env == null || env.trim().isEmpty()) {
            throw new RuntimeException(ENV + " must be set.");
        }

        return env;
    }

    private static long loadPeriod() {
        String value = getValue(PERIOD);
        long period;

        try {
            period = Long.parseLong(value);
        } catch (NumberFormatException e) {
            System.out.println(PERIOD + " must be a numeric value");
            throw e;
        }

        return period;
    }

    private static FileWriter openLogFileWriter() throws IOException {
        String fileName = getValue(LOG_FILE);
        FileWriter fileWriter;

        try {
            fileWriter = new FileWriter(fileName, true);
        } catch (IOException e) {
            System.out.println("Failed to open log file: " + fileName);
            throw e;
        }

        return fileWriter;
    }

    private static String getValue(String key) {
        String value = System.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("System Property: " + key + " must be set.");
        }

        return value;
    }
}
