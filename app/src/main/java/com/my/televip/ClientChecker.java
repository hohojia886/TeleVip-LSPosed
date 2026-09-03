package com.my.televip;

import com.my.televip.Clients.Cherrygram;
import com.my.televip.Clients.Forkgram;
import com.my.televip.Clients.ForkgramBeta;
import com.my.televip.Clients.ForkgramClassic;
import com.my.televip.Clients.IMe;
import com.my.televip.Clients.IMeWeb;
import com.my.televip.Clients.Momogram;
import com.my.televip.Clients.Nagram;
import com.my.televip.Clients.NagramX;
import com.my.televip.Clients.NagramXF;
import com.my.televip.Clients.Nekogram;
import com.my.televip.Clients.Nicegram;
import com.my.televip.Clients.TGConnect;
import com.my.televip.Clients.Telega;
import com.my.televip.Clients.Telegram;
import com.my.televip.Clients.TelegramBeta;
import com.my.televip.Clients.TelegramPlus;
import com.my.televip.Clients.TelegramWeb;
import com.my.televip.Clients.Telegraph;
import com.my.televip.Clients.Turrit;
import com.my.televip.Clients.XPlus;
import com.my.televip.utils.Utils;

import java.util.Arrays;

public class ClientChecker {
    public static boolean check(ClientType client, String pkgName)
    {
        return Arrays.asList(client.getPackageNames()).contains(pkgName);
    }

    public static boolean check(ClientType client)
    {
        return check(client, Utils.pkgName);
    }

    public static boolean isTgnetObfuscated()
    {
        return ClientType.fromPackage(Utils.pkgName).isTgnetObfuscated();
    }

    public enum ClientType {
        Telegram("org.telegram.messenger", Telegram.class),
        TelegramWeb("org.telegram.messenger.web", TelegramWeb.class),
        TelegramPlus("org.telegram.plus", TelegramPlus.class),
        TGConnect("com.tgconnect.android", TGConnect.class),
        Nagram("xyz.nextalone.nagram", Nagram.class),
        Nicegram("app.nicegram", Nicegram.class),
        TelegramBeta("org.telegram.messenger.beta", TelegramBeta.class),
        NagramX("nu.gpu.nagram", NagramX.class),
        XPlus("com.xplus.messenger", XPlus.class),
        iMe("com.iMe.android", IMe.class),
        iMeWeb("com.iMe.android.web", IMeWeb.class),
        forkgram("org.forkgram.messenger", Forkgram.class),
        forkgramBeta("org.forkclient.messenger.beta", ForkgramBeta.class),
        Telegraph("ir.ilmili.telegraph", Telegraph.class),
        Telega("ru.dahl.messenger", Telega.class),
        Momogram(new String[]{"nekox.messenger.broken", "momo.gram"}, Momogram.class),
        Nekogram("tw.nekomimi.nekogram", Nekogram.class, true),
        Cherrygram("uz.unnarsx.cherrygram", Cherrygram.class, true),
        ForkgramClassic("org.forkgram.classic", ForkgramClassic.class),
        Turrit("org.telegram.group", Turrit.class),
        NagramXF("fork.risin42.nagramx", NagramXF.class);

        private final String[] packageNames;
        private final Class<?> resolverClass;
        private final boolean tgnetObfuscated;

        ClientType(String packageName, Class<?> resolverClass) {
            this.packageNames = new String[]{packageName};
            this.resolverClass = resolverClass;
            tgnetObfuscated = false;
        }

        ClientType(String packageName, Class<?> resolverClass, boolean tgnetObfuscated) {
            this.packageNames = new String[]{packageName};
            this.resolverClass = resolverClass;
            this.tgnetObfuscated = tgnetObfuscated;
        }

        ClientType(String[] packageNames, Class<?> resolverClass) {
            this.packageNames = packageNames;
            this.resolverClass = resolverClass;
            tgnetObfuscated = false;
        }

        public String[] getPackageNames() { return packageNames; }
        public Class<?> getResolverClass() { return resolverClass; }
        public boolean isTgnetObfuscated() { return tgnetObfuscated; }

        public static ClientType fromPackage(String pkg){
            for (ClientType type: ClientType.values()){
                for (String name: type.getPackageNames()){
                    if (name.equals(pkg)) return type;
                }
            }
            return null;
        }

        public static boolean containsPackage(String pkg){
            return fromPackage(pkg) != null;
        }
    }
}
