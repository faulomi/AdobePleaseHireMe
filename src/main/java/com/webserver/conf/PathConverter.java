package com.webserver.conf;

import com.beust.jcommander.IStringConverter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by jmeuret on 29/06/2014.
 */
public class PathConverter implements IStringConverter<Path> {
    @Override
    public Path convert(String s) {
        return Paths.get(s);

    }
}
