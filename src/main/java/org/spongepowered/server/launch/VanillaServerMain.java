/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.server.launch;

import net.minecraft.launchwrapper.Launch;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class VanillaServerMain {

    private static final boolean VERIFY_CLASSPATH = !System.getProperty("spongevanilla.verify_classpath", "true").equals("false");
    private static final boolean AUTO_DOWNLOAD_DEPS = !System.getProperty("spongevanilla.auto_dl", "true").equals("false");

    private static final String LIBRARIES_DIR = "libraries";

    private static final String MINECRAFT_SERVER_LOCAL = "minecraft_server.1.8.jar";
    private static final String MINECRAFT_SERVER_REMOTE = "https://s3.amazonaws.com/Minecraft.Download/versions/1.8/minecraft_server.1.8.jar";

    private static final String LAUNCHWRAPPER_PATH = "/net/minecraft/launchwrapper/1.12/launchwrapper-1.12.jar";
    private static final String LAUNCHWRAPPER_LOCAL = LIBRARIES_DIR + LAUNCHWRAPPER_PATH;
    private static final String LAUNCHWRAPPER_REMOTE = "https://libraries.minecraft.net" + LAUNCHWRAPPER_PATH;

    private static final String TWEAKER = "org.spongepowered.server.launch.VanillaServerTweaker";

    private VanillaServerMain() {
    }

    public static void main(String[] args) throws Exception {
        if (VERIFY_CLASSPATH) {
            Path base = Paths.get(VanillaServerMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();

            try {
                if (!downloadMinecraft(base)) {
                    System.err.println("Failed to load all required dependencies. Please download them manually:");
                    System.err.println("Download " + MINECRAFT_SERVER_REMOTE + " and copy it to "
                            + base.resolve(MINECRAFT_SERVER_LOCAL).toAbsolutePath());
                    System.err.println("Download " + LAUNCHWRAPPER_REMOTE + " and copy it to "
                            + base.resolve(LAUNCHWRAPPER_LOCAL).toAbsolutePath());
                    System.exit(1);
                    return;
                }
            } catch (IOException e) {
                System.err.println("Failed to download required dependencies. Please try again later.");
                e.printStackTrace();
                System.exit(1);
                return;
            }
        } else {
            System.err.println("Classpath verification is disabled. The server may NOT start properly unless you have all required dependencies on "
                    + "the classpath!");
        }


        Launch.main(join(args,
                "--tweakClass", TWEAKER
        ));
    }

    private static String[] join(String[] args, String... prefix) {
        String[] result = new String[prefix.length + args.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(args, 0, result, prefix.length, args.length);
        return result;
    }

    private static boolean downloadMinecraft(Path base) throws IOException, NoSuchAlgorithmException {
        // Make sure the Minecraft server is available, or download it otherwise
        Path path = base.resolve(MINECRAFT_SERVER_LOCAL);
        if (Files.notExists(path) && (!AUTO_DOWNLOAD_DEPS || !downloadVerified(MINECRAFT_SERVER_REMOTE, path))) {
            return false;
        }

        // Make sure Launchwrapper is available, or download it otherwise
        path = base.resolve(LAUNCHWRAPPER_LOCAL);
        return Files.exists(path) || (AUTO_DOWNLOAD_DEPS && downloadVerified(LAUNCHWRAPPER_REMOTE, path));
    }


    private static boolean downloadVerified(String remote, Path path) throws IOException, NoSuchAlgorithmException {
        Files.createDirectories(path.getParent());

        String name = path.getFileName().toString();
        URL url = new URL(remote);

        System.out.println("Downloading " + name + "... This can take a while.");
        System.out.println(url);
        URLConnection con = url.openConnection();
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        try (ReadableByteChannel source = Channels.newChannel(new DigestInputStream(con.getInputStream(), md5));
             FileChannel out = FileChannel.open(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            out.transferFrom(source, 0, Long.MAX_VALUE);
        }

        String expected = getETag(con);
        if (!expected.isEmpty()) {
            String hash = toHexString(md5.digest());
            if (hash.equals(expected)) {
                System.out.println("Successfully downloaded " + name + " and verified checksum!");
            } else {
                Files.delete(path);
                throw new IOException("Checksum verification failed: Expected " + expected + ", got " + hash);
            }
        }

        return true;
    }

    private static String getETag(URLConnection con) {
        String hash = con.getHeaderField("ETag");
        if (hash == null || hash.isEmpty()) {
            return "";
        }

        if (hash.startsWith("\"") && hash.endsWith("\"")) {
            hash = hash.substring(1, hash.length() - 1);
        }

        return hash;
    }

    // From http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
    private static final char[] hexArray = "0123456789abcdef".toCharArray();

    public static String toHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
