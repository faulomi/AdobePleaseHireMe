package fr.meuret.web.conf;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.validators.PositiveInteger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The configuration for the http server.
 * <p/>
 * Basically, there are three parameters :
 * <p/>
 * <ol>
 * <li>port : defaulted to <i>8080</i></li>
 * <li>rootPath : path for the static files directory. Defaulted to the working directory path.</li>
 * <li>useSSL : if the http server runs on SSL. Defaulted to <i>false</i>. </li>
 * </ol>
 * <p/>
 * This class is using the Builder Pattern described  in the Effective Java book written by Joshua Bloch.
 */
public class HttpConfiguration {


    private final int port;
    private final Path rootPath;
    private final boolean useSSL;


    private HttpConfiguration(Builder builder) {

        this.port = builder.port;
        this.rootPath = builder.rootPath;
        this.useSSL = builder.useSSL;

    }

    public static HttpConfiguration defaultConfiguration() {

        return new Builder().build();
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


        @Parameter(names = {"-port"}, description = "Http server listening port.", validateWith = PositiveInteger.class)
        private int port = 8080;
        @Parameter(names = {"-rootPath"}, description = "Static files root path.", converter = PathConverter.class)
        private Path rootPath = Paths.get(System.getProperty("user.dir"));
        @Parameter(names = {"-useSSL"}, description = "SSL mode.")
        private Boolean useSSL = false;


        public Builder() {


        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Builder{");
            sb.append("port=").append(port);
            sb.append(", rootPath=").append(rootPath);
            sb.append(", useSSL=").append(useSSL);
            sb.append('}');
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Builder builder = (Builder) o;

            if (port != builder.port) return false;
            if (rootPath != null ? !rootPath.equals(builder.rootPath) : builder.rootPath != null) return false;
            if (useSSL != null ? !useSSL.equals(builder.useSSL) : builder.useSSL != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = port;
            result = 31 * result + (rootPath != null ? rootPath.hashCode() : 0);
            result = 31 * result + (useSSL != null ? useSSL.hashCode() : 0);
            return result;
        }

        public Builder port(int port) {
            this.port = port;

            return this;
        }

        public Builder rootPath(String rootPath) {
            if (rootPath != null)
                this.rootPath = Paths.get(rootPath);
            return this;

        }

        public Builder useSSL(boolean useSSL) {

            this.useSSL = useSSL;
            return this;
        }


        public HttpConfiguration build() {

            //Check rootPath
            if (Files.notExists(rootPath) || !Files.isDirectory(rootPath))
                throw new IllegalArgumentException("Invalid value for rootpath directive : " + rootPath);
            return new HttpConfiguration(this);
        }

    }


}
