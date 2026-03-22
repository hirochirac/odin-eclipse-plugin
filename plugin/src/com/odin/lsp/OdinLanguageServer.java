package com.odin.lsp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;

public class OdinLanguageServer extends ProcessStreamConnectionProvider {
    public OdinLanguageServer() {
        List<String> commands = new ArrayList<>();
        String olsPath = System.getenv("OLS_PATH");
        
        if (olsPath == null || olsPath.isEmpty()) {
            olsPath = "I:\\lang\\ols\\ols.exe";
        }
        
        File olsFile = new File(olsPath);
        if (!olsFile.exists()) {
            System.err.println("[OLS] ERROR: OLS not found at: " + olsPath);
            olsPath = "ols";
        }
        
        commands.add(olsPath);
        
        String odinRoot = System.getenv("ODIN_ROOT");
        if (odinRoot == null || odinRoot.isEmpty()) {
            odinRoot = findOdinRoot();
        }
        
        if (odinRoot != null && !odinRoot.isEmpty()) {
            commands.add("-collection:core=" + odinRoot + "/core");
            commands.add("-collection:vendor=" + odinRoot + "/vendor");
        } else {
            System.err.println("[OLS] WARNING: ODIN_ROOT not found. Collections won't work.");
        }
        
        setCommands(commands);
        
        System.out.println("[OLS] Using OLS: " + olsPath);
        System.out.println("[OLS] ODIN_ROOT: " + odinRoot);
        System.out.println("[OLS] Commands: " + commands);
    }

    private String findOdinRoot() {
        String[] paths = {
            "C:\\Odin",
            "C:\\Program Files\\Odin",
            System.getProperty("user.home") + "\\Odin",
            "I:\\lang\\Odin"
        };
        
        for (String path : paths) {
            File coreDir = new File(path, "core");
            if (coreDir.exists() && coreDir.isDirectory()) {
                System.out.println("[OLS] Found ODIN_ROOT at: " + path);
                return path;
            }
        }
        return null;
    }

    @Override
    public void start() throws IOException {
        System.out.println("[OLS] Starting Odin Language Server...");
        try {
            super.start();
            System.out.println("[OLS] Language Server started successfully");
        } catch (IOException e) {
            System.err.println("[OLS] Failed to start: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
