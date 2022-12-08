package org.odoral.adventofcode.y2022.day7;

import org.odoral.adventofcode.common.CommonUtils;
import org.odoral.adventofcode.common.model.Directory;
import org.odoral.adventofcode.common.model.File;
import org.odoral.adventofcode.common.model.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoSpaceLeftOnDevice {

    public static final String ROOT_NAME = "/";
    public static final String ONE_LEVEL_OUT = "..";

    public static final long TOTAL_SIZE_100K = 100000L;
    public static final long TOTAL_DISK_SPACE = 70000000L;
    public static final long TOTAL_SPACE_TO_FREE = 30000000L;

    public static void main(String[] args) throws IOException {
        NoSpaceLeftOnDevice noSpaceLeftOnDevice = new NoSpaceLeftOnDevice();
        Directory root = noSpaceLeftOnDevice.buildFileSystem(CommonUtils.loadResource("/input.txt", Function.identity()));
        noSpaceLeftOnDevice.printFileSystem(root);
        Result result = noSpaceLeftOnDevice.findAllDirectoriesWithAtMost(root, TOTAL_SIZE_100K);
        log.info("Directories to remove: {} for a total: {}", result.directories, result.total);

        result = noSpaceLeftOnDevice.findDirectoriesToFree(root, TOTAL_SPACE_TO_FREE);
        log.info("Directories to remove: {} for a total: {}", result.directories, result.total);
    }

    protected Directory buildFileSystem(List<String> commands) {
        Context context = new Context();
        for (String command : commands) {
            switch (command.charAt(0)) {
                case '$':
                    context = runCommand(context, command);
                    break;
                case 'd':
                default:
                    context = parseLsCommand(context, command);
            }
        }

        return context.root;
    }

    protected Context runCommand(Context context, String command) {
        String[] fields = command.split("\\s");
        switch (fields[1]) {
            case "cd":
                context = runCdCommand(context, fields[2]);
                break;
            case "ls":
                // None
        }
        return context;
    }

    protected Context runCdCommand(Context context, String dirName) {
        switch (dirName) {
            case ROOT_NAME:
                context.currentNode = context.root;
                break;
            case ONE_LEVEL_OUT:
                context.currentNode = context.currentNode.parent;
                break;
            default:
                context.currentNode = context.currentNode.childs.get(dirName);
        }

        return context;
    }

    protected Context parseLsCommand(Context context, String command) {
        String[] fields = command.split("\\s");
        if (fields[0].equals("dir")) {
            String dirName = fields[1];
            context.currentNode.childs.put(dirName, new Directory(dirName, context.currentNode));
        } else {
            long size = Long.parseLong(fields[0]);
            String fileName = fields[1];
            context.currentNode.childs.put(fileName, new File(fileName, context.currentNode, size));
        }
        return context;
    }

    protected void printFileSystem(Node node) {
        log.info("- " + node.name + " (dir)");
        printFileSystem(node, 0);
    }

    protected void printFileSystem(Node node, int level) {
        node.childs.values().stream()
            .sorted(Comparator.comparing(n -> n.name))
            .forEach(n -> {
                StringBuilder tabs = new StringBuilder();
                for (int i = 0; i <= level; i++) {
                    tabs.append("\t");
                }
                if (n instanceof Directory) {
                    Directory directory = (Directory) n;
                    log.info(tabs + "- " + directory.name + " (dir)");
                    printFileSystem(directory, level + 1);
                } else {
                    if (n instanceof File) {
                        File file = (File) n;
                        log.info(tabs + "- " + file.name + " (file, size=" + file.size + ")");

                    }
                }
            });
    }

    protected Result findAllDirectoriesWithAtMost(Node root, long totalSize) {
        Result result = new Result();

        result.directories = findDirectories(root);
        result.total = result.directories.stream()
            .filter(directory -> directory.size() <= totalSize)
            .mapToLong(Directory::size)
            .sum();

        return result;
    }

    protected List<Directory> findDirectories(Node node) {
        List<Directory> directories = new ArrayList<>();
        for (Node value : node.childs.values()) {
            if (value instanceof Directory) {
                Directory directory = (Directory) value;
                directories.add(directory);
                directories.addAll(findDirectories(directory));
            }
        }
        return directories;
    }

    protected Result findDirectoriesToFree(Directory root, long totalSizeToFree) {
        Result result = new Result();

        log.info("Root size: {}", root.size());
        final long totalSizeToDelete = root.size() + totalSizeToFree - TOTAL_DISK_SPACE;
        log.info("Need to free: {}", totalSizeToDelete);
        AtomicLong total = new AtomicLong();
        findDirectories(root).stream()
            .sorted(Comparator.comparing(Directory::size))
            .filter(directory -> directory.size() > totalSizeToDelete)
            .limit(1)
            .forEach(directory -> {
                total.addAndGet(directory.size());
                result.directories.add(directory);
            });

        result.total = total.get();
        return result;
    }

    public static class Context {
        Directory root = new Directory(ROOT_NAME, null);
        Node currentNode = root;
    }

    public static class Result {
        List<Directory> directories = new ArrayList<>();
        long total;
    }
}