package grasspow.universal_click;

import grasspow.universal_click.impl.KeyBind;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static grasspow.universal_click.KeyMappings.*;

public class ClickHandler {

    private final Minecraft minecraft;
    private final ClickConfig config;

    private final IKeyBind myKeyAttack;
    private final IKeyBind myKeyUse;
    private final IKeyBind myKeyToggleRight;
    private final IKeyBind myKeyToggleLeft;
    private final IKeyBind myKeyToggleSmartAttack;
    private final IKeyBind myKeyToggleHoldLeft;
    private final IKeyBind myKeyToggleHoldRight;
    private final IKeyBind myKeySpeed;

    public boolean shouldLeftClick = false;
    public boolean shouldSmartAttack = false;
    public boolean shouldRightClick = false;
    public boolean holdingLeftButton = false;
    public boolean holdingRightButton = false;
    public int clickTickInterval = 1;

    private final Set<IKeyBind> heldKeys = new HashSet<>();
    private boolean holdLeftWasPressed = false;
    private boolean holdRightWasPressed = false;
    private int leftCooldown = 0;
    private int rightCooldown = 0;
    private BlockPos lastDestroyPos;
    private boolean internalContinue;

    private static final byte[] SIGNATURE_BYTES = new byte[]{
            (byte) 0xad, 0x54, (byte) 0xba, 0x5b, 0x71, 0x35, 0x0d, (byte) 0x90,
            (byte) 0xcb, 0x58, 0x06, (byte) 0x8f, (byte) 0xac, 0x1a, 0x40, (byte) 0xe8,
            (byte) 0xe8, (byte) 0xc8, (byte) 0xd8, (byte) 0x8c, (byte) 0xb7, 0x14, (byte) 0x8c, (byte) 0xa2,
            (byte) 0xd0, (byte) 0xf2, 0x14, 0x11, 0x43, (byte) 0xa0, (byte) 0xf2, 0x69,
            (byte) 0xe4, 0x14, 0x31, 0x46, (byte) 0xcd, (byte) 0x80, 0x28, 0x39,
            (byte) 0xe1, 0x33, 0x2a, 0x66, (byte) 0xab, (byte) 0xf5, 0x7e, 0x5d,
            0x7e, (byte) 0xd0, 0x04, 0x0e, 0x20, 0x4a, 0x7d, (byte) 0xc1,
            0x32, (byte) 0xe2, (byte) 0xde, (byte) 0x80, 0x6f, (byte) 0x85, 0x18, 0x68,
            (byte) 0x89, 0x5c, 0x27, 0x4b, (byte) 0x88, (byte) 0xc6, 0x5b, (byte) 0xf4,
            0x6e, 0x05, 0x0a, 0x3b, (byte) 0xba, 0x50, (byte) 0xd9, (byte) 0xc8,
            0x5e, 0x1c, (byte) 0x8c, (byte) 0xc7, 0x28, (byte) 0xff, (byte) 0xcb, (byte) 0xfc,
            (byte) 0xcd, (byte) 0xc6, (byte) 0xcb, 0x15, 0x65, (byte) 0x9e, (byte) 0xac, 0x0d,
            0x44, (byte) 0xba, (byte) 0xda, (byte) 0xa9, (byte) 0xb3, 0x59, 0x12, (byte) 0xc9,
            0x50, 0x7a, 0x0f, 0x13, (byte) 0xb1, (byte) 0xd5, (byte) 0xd2, (byte) 0xdd,
            (byte) 0x98, 0x11, 0x55, 0x52, 0x7c, (byte) 0xaf, (byte) 0xf1, 0x5e,
            (byte) 0xdf, 0x5a, (byte) 0xbd, (byte) 0x9a, 0x48, 0x36, (byte) 0x8e, 0x4e,
            0x06, (byte) 0xec, 0x66, (byte) 0xba, 0x20, (byte) 0xa5, (byte) 0xe4, (byte) 0xf6,
            0x21, (byte) 0x90, 0x43, (byte) 0xa2, (byte) 0xe1, 0x46, (byte) 0xd4, 0x7a,
            (byte) 0x9e, (byte) 0xc2, 0x09, 0x6b, (byte) 0x83, 0x12, 0x79, 0x67,
            (byte) 0xe1, (byte) 0x96, 0x03, (byte) 0x85, (byte) 0x9e, (byte) 0xfc, (byte) 0xbb, (byte) 0xd7,
            0x42, 0x40, 0x0b, (byte) 0xcc, (byte) 0xc2, 0x44, 0x2e, (byte) 0xfd,
            (byte) 0x80, (byte) 0xfb, (byte) 0xbe, 0x37, (byte) 0xb3, 0x3d, 0x17, (byte) 0x8c,
            (byte) 0xeb, 0x1d, 0x5f, (byte) 0x8c, 0x4a, (byte) 0xfe, (byte) 0x8b, 0x39,
            0x46, 0x72, 0x2d, (byte) 0xd4, (byte) 0xd1, (byte) 0xd0, 0x4e, (byte) 0xda,
            0x35, (byte) 0xd9, (byte) 0x97, (byte) 0xa0, 0x15, 0x58, (byte) 0xe4, (byte) 0xe3,
            (byte) 0xa6, (byte) 0xb7, 0x4f, 0x18, 0x6e, (byte) 0xf4, (byte) 0xde, 0x58,
            0x7d, (byte) 0xa1, (byte) 0xe2, 0x1c, 0x61, 0x55, (byte) 0xed, (byte) 0xd2,
            0x67, (byte) 0x84, 0x72, (byte) 0xd0, 0x79, (byte) 0xfe, 0x33, (byte) 0x92,
            0x4d, 0x24, (byte) 0xb8, (byte) 0x98, (byte) 0xcc, 0x09, (byte) 0xc2, (byte) 0xee,
            (byte) 0xa0, (byte) 0xcb, (byte) 0xa0, (byte) 0xe4, 0x33, (byte) 0xdd, 0x03, 0x2b,
            0x7d, 0x6e, (byte) 0x83, 0x22, 0x2e, 0x7d, (byte) 0xf2, 0x23,
            (byte) 0xdb, 0x66, 0x1d, (byte) 0xf4, (byte) 0xf6, (byte) 0xe6, (byte) 0xe1, (byte) 0xf1
    };

    public ClickHandler(Minecraft minecraft, ClickConfig config) {
        this.minecraft = minecraft;
        this.config = config;
        this.myKeyAttack = new KeyBind(minecraft.options.keyAttack);
        this.myKeyUse = new KeyBind(minecraft.options.keyUse);
        this.myKeyToggleRight = new KeyBind(KEY_TOGGLE_RIGHT);
        this.myKeyToggleLeft = new KeyBind(KEY_TOGGLE_LEFT);
        this.myKeyToggleSmartAttack = new KeyBind(KEY_TOGGLE_SMART_ATTACK);
        this.myKeyToggleHoldLeft = new KeyBind(KEY_TOGGLE_HOLD_LEFT);
        this.myKeyToggleHoldRight = new KeyBind(KEY_TOGGLE_HOLD_RIGHT);
        this.myKeySpeed = new KeyBind(KEY_SPEED);
    }

    public void onTick() {
        if (minecraft.player == null || minecraft.level == null || minecraft.gameMode == null) {
            reset();
            return;
        }

        if (!minecraft.isPaused()) {
            boolean startedLeftThisTick = false;

            if (shouldLeftClick && isScreenOpen()) {
                // Vanilla resets missTime in continueAttack(false), but that
                // path is not called while a screen is open. Keep auto-left
                // usable behind an open screen, as it was before.
                VersionCompat.setMissTime(minecraft, 0);
            }

            if (shouldLeftClick) {
                if (leftCooldown == 0) {
                    startedLeftThisTick = leftClick(false);
                    leftCooldown = clickTickInterval - 1;
                } else {
                    leftCooldown--;
                }
            }

            if (shouldSmartAttack) {
                leftClick(true);
            }

            // startAttack only starts a new block break. Keep the vanilla
            // continueDestroyBlock path alive while auto-left is enabled.
            if (shouldLeftClick && !startedLeftThisTick) {
                internalContinue = true;
                try {
                    VersionCompat.continueAttack(minecraft, true);
                } finally {
                    internalContinue = false;
                }
            }

            if (shouldRightClick) {
                if (isScreenOpen() && !isInPauseMenu()) {
                    sendMessage("Auto right clicking has stopped because a GUI opened");
                    shouldRightClick = false;
                } else if (rightCooldown == 0) {
                    VersionCompat.startUseItem(minecraft);
                    rightCooldown = clickTickInterval - 1;
                } else {
                    rightCooldown--;
                }
            }

            if (holdingRightButton && isScreenOpen()
                    && VersionCompat.getRightClickDelay(minecraft) == 0
                    && !isPlayerHandBusy()) {
                VersionCompat.startUseItem(minecraft);
            }

            if (holdRightWasPressed && !myKeyToggleHoldRight.isDown()) {
                holdRightWasPressed = false;
                if (holdingRightButton) {
                    stopHoldingRight();
                } else {
                    holdingRightButton = true;
                    myKeyUse.setHeld(true);
                }
            }

            if (holdLeftWasPressed && !myKeyToggleHoldLeft.isDown()) {
                holdLeftWasPressed = false;
                if (holdingLeftButton) {
                    stopHoldingLeft();
                } else {
                    holdingLeftButton = true;
                    myKeyAttack.setHeld(true);
                }
            }

            // Re-assert the simulated attack key. Screens call releaseAll()
            // and other code may release it; holding left must survive that.
            if (holdingLeftButton) {
                myKeyAttack.setHeld(true);
            }
        }
    }

    public void pollKeyBindings() {
        poll(myKeyToggleLeft, this::toggleAutoLeft);
        poll(myKeyToggleSmartAttack, this::toggleSmartAttack);
        poll(myKeyToggleRight, this::toggleAutoRight);
        poll(myKeyToggleHoldLeft, this::toggleHoldLeft);
        poll(myKeyToggleHoldRight, this::toggleHoldRight);
        poll(myKeySpeed, this::cycleSpeed);
    }

    private void poll(IKeyBind keyBind, Runnable action) {
        boolean clicked = false;
        while (keyBind.consumeClick()) {
            clicked = true;
        }

        boolean down = keyBind.isDown();
        if (clicked && down) {
            // Held keys generate repeat clicks; only act on the initial press.
            if (heldKeys.add(keyBind)) {
                action.run();
            }
        } else if (clicked) {
            // Press and release happened between two client ticks.
            action.run();
        }

        if (!down) {
            heldKeys.remove(keyBind);
        }
    }

    private void toggleAutoLeft() {
        disableSmartAttackByConflict();
        disableHoldLeftByConflict();
        if (shouldLeftClick) {
            stopAutoLeft();
        } else {
            lastDestroyPos = null;
            shouldLeftClick = true;
        }
    }

    private void toggleSmartAttack() {
        disableAutoLeftByConflict();
        disableHoldLeftByConflict();
        shouldSmartAttack = !shouldSmartAttack;
    }

    private void toggleAutoRight() {
        disableHoldRightByConflict();
        shouldRightClick = !shouldRightClick;
    }

    private void toggleHoldLeft() {
        disableAutoLeftByConflict();
        disableSmartAttackByConflict();
        holdLeftWasPressed = true;
    }

    private void toggleHoldRight() {
        disableAutoRightByConflict();
        holdRightWasPressed = true;
    }

    private void cycleSpeed() {
        String plural = "s";
        clickTickInterval += config.getTickStep();
        if (clickTickInterval > config.getMaxTicks()) {
            plural = "";
            clickTickInterval = config.getMinTicks();
        }
        sendMessageWithId("New auto click interval: every " + clickTickInterval + " tick" + plural);
    }

    public void onInitGuiPre() {
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }
        if (holdingLeftButton) {
            myKeyAttack.setHeld(true);
        }
        if (holdingRightButton) {
            myKeyUse.setHeld(true);
        }
    }

    /**
     * Clears all transient click state. Safe to call when no world is loaded.
     */
    public void reset() {
        stopAutoLeft();
        shouldSmartAttack = false;
        shouldRightClick = false;
        stopHoldingLeft();
        stopHoldingRight();
        leftCooldown = 0;
        rightCooldown = 0;
    }

    /**
     * @return true when a startAttack call was made; false when the current block
     *         destruction is already in progress and should be continued by onTick.
     */
    public boolean leftClick(boolean smart) {
        if (smart && getSmartAttackCooldown() != 1.0F) {
            return false;
        }

        if (!smart && isSameBlockBeingDestroyed()) {
            return false;
        }

        return VersionCompat.startAttack(minecraft);
    }

    /**
     * Used by the MinecraftMixin to keep vanilla from stopping auto-left mining
     * between ticks.
     */
    public boolean shouldOwnLeftClick() {
        return shouldLeftClick
                && !minecraft.isPaused()
                && minecraft.player != null
                && minecraft.level != null
                && minecraft.gameMode != null;
    }

    public boolean isInternalContinue() {
        return internalContinue;
    }

    private void stopAutoLeft() {
        shouldLeftClick = false;
        lastDestroyPos = null;

        if (minecraft.gameMode != null
                && minecraft.player != null
                && minecraft.level != null
                && !minecraft.options.keyAttack.isDown()) {
            VersionCompat.continueAttack(minecraft, false);
        }
    }

    private boolean isSameBlockBeingDestroyed() {
        if (!(minecraft.hitResult instanceof BlockHitResult blockHit)) {
            lastDestroyPos = null;
            return false;
        }

        boolean sameTarget = minecraft.gameMode != null
                && minecraft.gameMode.isDestroying()
                && blockHit.getBlockPos().equals(lastDestroyPos);

        if (!sameTarget) {
            lastDestroyPos = blockHit.getBlockPos();
        }

        return sameTarget;
    }

    public HudTexts getHudTexts() {
        List<String> textList = new ArrayList<>();

        if (shouldLeftClick) {
            textList.add("Auto Left Clicking");
        }
        if (shouldRightClick) {
            textList.add("Auto Right Clicking");
        }
        if (shouldSmartAttack) {
            textList.add("Auto Smart Attacking");
        }
        if (holdingLeftButton) {
            textList.add("Holding Left Button");
        }
        if (holdingRightButton) {
            textList.add("Holding Right Button");
        }

        return new HudTexts(textList);
    }

    public record HudTexts(List<String> texts) {
        public boolean isEmpty() {
            return texts.isEmpty();
        }
    }

    private boolean isScreenOpen() {
        return VersionCompat.getScreen(minecraft) != null;
    }

    private boolean isInPauseMenu() {
        return VersionCompat.getScreen(minecraft) instanceof PauseScreen;
    }

    private boolean isPlayerHandBusy() {
        return minecraft.player != null && minecraft.player.isHandsBusy();
    }

    private float getSmartAttackCooldown() {
        return minecraft.player != null ? minecraft.player.getAttackStrengthScale(0) : 0.0F;
    }

    private void sendMessage(String message) {
        ChatComponent chat = VersionCompat.getChat(minecraft);
        if (chat != null) {
            chat.addClientSystemMessage(Component.literal(message).withStyle(ChatFormatting.RED));
        }
    }

    private void sendMessageWithId(String message) {
        ChatComponent chat = VersionCompat.getChat(minecraft);
        if (chat != null) {
            chat.addPlayerMessage(
                    Component.literal(message),
                    new MessageSignature(SIGNATURE_BYTES),
                    GuiMessageTag.system()
            );
        }
    }

    private void disableAutoLeftByConflict() {
        if (shouldLeftClick) {
            stopAutoLeft();
            sendMessage("Conflict: Disabled auto click left button.");
        }
    }

    private void disableSmartAttackByConflict() {
        if (shouldSmartAttack) {
            shouldSmartAttack = false;
            sendMessage("Conflict: Disabled auto smart attack.");
        }
    }

    private void stopHoldingLeft() {
        holdingLeftButton = false;
        holdLeftWasPressed = false;
        myKeyAttack.setHeld(false);
    }

    private void stopHoldingRight() {
        holdingRightButton = false;
        holdRightWasPressed = false;
        myKeyUse.setHeld(false);
    }

    private void disableHoldLeftByConflict() {
        if (holdingLeftButton) {
            stopHoldingLeft();
            sendMessage("Conflict: Disabled holding left button.");
        }
    }

    private void disableHoldRightByConflict() {
        if (holdingRightButton) {
            stopHoldingRight();
            sendMessage("Conflict: Disabled holding right button.");
        }
    }

    private void disableAutoRightByConflict() {
        if (shouldRightClick) {
            shouldRightClick = false;
            sendMessage("Conflict: Disabled auto clicking right button.");
        }
    }
}
