package com.webserver.conf;

import com.webserver.HTTPServer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by jmeuret on 28/06/2014.
 */
public class HTTPConfiguration {

    private final int port;
    private final Path rootPath;
    private final boolean useSSL;




    public static HTTPConfiguration defaultConfiguration(){

        return new Builder().build();
    }


    private HTTPConfiguration(Builder builder){

        this.port = builder.port;
        this.rootPath = builder.rootPath;
        this.useSSL = builder.useSSL;

    }

    public boolean useSSL() {
        return useSSL;
    }

    public Path getRootPath() {
        return rootPath;
    }

    public int getPort() {
        return port;
    }

    public static class Builder {

        private int port = 8080;
        private Path rootPath= Paths.get("");
        private boolean useSSL = false;


        public Builder(){


        }

        public Builder(String properties){


        }

        public Builder port(int port){
            this.port = port;

            return this;
        }

        public Builder rootPath(String rootPath){
            if(rootPath != null)
                this.rootPath = Paths.get(rootPath);
            return this;

        }

        public Builder useSSL(boolean useSSL){

            this.useSSL = useSSL;
            return this;
        }



        public HTTPConfiguration build(){


            //Check rootPath
            if(Files.notExists(rootPath) || !Files.isDirectory(rootPath))
                throw new IllegalArgumentException("Invalid value for rootpath directive : " + rootPath);


            return new HTTPConfiguration(this);

        }





    }




}
