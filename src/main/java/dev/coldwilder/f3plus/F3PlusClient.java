package dev.coldwilder.f3plus;

import net.fabricmc.api.ClientModInitializer;

public final class F3PlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        F3PlusConfig.load();
    }
}
