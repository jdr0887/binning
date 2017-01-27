package org.renci.binning.core;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.renci.common.exec.BashExecutor;
import org.renci.common.exec.CommandInput;
import org.renci.common.exec.CommandOutput;
import org.renci.common.exec.ExecutorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IRODSUtils {

    private static final Logger logger = LoggerFactory.getLogger(IRODSUtils.class);

    public static String findFile(Map<String, String> avuMap) throws BinningException {
        logger.debug("ENTERING findFile(Map<String, String>)");
        return findFile(avuMap, null);
    }

    public static String findFile(Map<String, String> avuMap, String extension) throws BinningException {
        logger.debug("ENTERING findFile(Map<String, String>, String)");
        StringBuilder commandSB = new StringBuilder("$BINNING_IRODS_HOME/imeta -d qu ");
        for (String key : avuMap.keySet()) {
            commandSB.append(String.format(" and %s = '%s'", key, avuMap.get(key)));
        }

        String irodsFile = null;
        try {
            CommandInput lookupFileCI = new CommandInput(commandSB.toString().replaceFirst(" and ", ""));
            CommandOutput lookupFileCO = BashExecutor.getInstance().execute(lookupFileCI,
                    new File(System.getProperty("user.home"), ".binningrc"));
            String output = lookupFileCO.getStdout().toString();
            List<String> lines = Arrays.asList(output.split("\n"));

            String directory = null;
            String file = null;

            if (StringUtils.isNotEmpty(extension) && lines.size() > 3) {

                for (String line : lines) {
                    if (line.contains("collection")) {
                        directory = line.split(":")[1];
                    }
                    if (line.contains("dataObj") && line.endsWith(extension)) {
                        file = line.split(":")[1];
                        break;
                    }
                }

            } else {
                for (String line : lines) {
                    if (line.contains("collection")) {
                        directory = line.split(":")[1];
                    }
                    if (line.contains("dataObj")) {
                        file = line.split(":")[1];
                    }
                }
            }

            if (StringUtils.isNotEmpty(directory) && StringUtils.isNotEmpty(file)) {
                irodsFile = String.format("%s/%s", directory.trim(), file.trim());
            }
        } catch (ExecutorException e) {
            throw new BinningException(e);
        }
        return irodsFile;
    }

    public static File getFile(String irodsFile, String outputDir) throws BinningException {
        logger.debug("ENTERING getFile(String, String)");
        try {
            CommandInput getCI = new CommandInput(String.format("$BINNING_IRODS_HOME/iget -N 1 %s %s", irodsFile, outputDir));
            BashExecutor.getInstance().execute(getCI, new File(System.getProperty("user.home"), ".binningrc"));
        } catch (ExecutorException e) {
            throw new BinningException(e);
        }
        return new File(outputDir, irodsFile.substring(irodsFile.lastIndexOf("/"), irodsFile.length()));
    }

}
