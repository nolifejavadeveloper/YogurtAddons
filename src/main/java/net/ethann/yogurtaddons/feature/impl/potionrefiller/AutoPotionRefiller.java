package net.ethann.yogurtaddons.feature.impl.potionrefiller;

import net.ethann.yogurtaddons.feature.impl.potionrefiller.chestinput.ChestInput;
import net.ethann.yogurtaddons.feature.impl.potionrefiller.chestinput.InputChestCommand;
import net.ethann.yogurtaddons.feature.impl.potionrefiller.chestinput.InputChestManager;
import net.ethann.yogurtaddons.feature.impl.potionrefiller.chestinput.InputChestRenderer;
import net.ethann.yogurtaddons.util.*;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;
import java.util.Queue;

public class AutoPotionRefiller extends ManagedTask {
    private final InputChestManager inputChestManager;
    private final InputChestRenderer inputChestRenderer;
    private final AutoChestRefiller autoChestRefiller;

    private final Scheduler scheduler;

    private final Queue<ChestInput> chestQueue = new LinkedList<>();

    private boolean nextMoveAllowed;

    private RefillStage stage;

    public AutoPotionRefiller() {
        this.scheduler = new Scheduler();

        this.inputChestManager = new InputChestManager();
        this.inputChestRenderer = new InputChestRenderer(chestQueue, inputChestManager);

        this.autoChestRefiller = new AutoChestRefiller(this::onFinish);
//        this.chestNavigator = new ChestNavigator(this::onFinish);

        ClientCommandHandler.instance.registerCommand(new InputChestCommand(inputChestManager, inputChestRenderer));
        ClientCommandHandler.instance.registerCommand(new AutoPotionRefillerCommand(autoChestRefiller));
    }

    protected void onEnable() {
        ChatUtil.log("enabling potionrefiller");
        chestQueue.clear();
        if (!inputChestManager.isGroupSet()) {
            ChatUtil.warn("please select a group");
            disable();
            return;
        }

        chestQueue.addAll(inputChestManager.getAll());

        nextMoveAllowed = true;

        inputChestRenderer.startRendering();
        inputChestRenderer.setRenderingType(InputChestRenderer.RenderType.NEXT);

        scheduler.schedule(1000);

        MinecraftForge.EVENT_BUS.register(this);

        stage = RefillStage.NAVIGATE;
    }

    protected void onDisable() {
        ChatUtil.log("disabling potionrefiller");
        MinecraftForge.EVENT_BUS.unregister(this);

        inputChestRenderer.stopRendering();
    }

    @Override
    protected String getName() {
        return "Potion Refiller";
    }

    public void onFinish(boolean success) {
        if (!success) {
            disable();
            return;
        }

        switch (stage) {
            case NAVIGATE: {
                scheduler.schedule(DelayUtil.getDelay(200, 100));

                stage = RefillStage.FILL;
                chestQueue.remove();
                break;
            }
            case FILL: {
                scheduler.schedule(DelayUtil.getDelay(200, 100));

                stage = RefillStage.NAVIGATE;
                break;
            }
        }

        nextMoveAllowed = true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (!nextMoveAllowed) return;
        if (!scheduler.isOver()) return;
        nextMoveAllowed = false;

        switch (stage) {
            case FILL: {
                autoChestRefiller.enable();
                break;
            }
            case NAVIGATE: {

                ChestInput pos = chestQueue.peek();
                if (pos == null) {
                    ChatUtil.log("refill completed, stopping...");
                    disable();
                    return;
                }
//                chestNavigator.go(pos);
                break;
            }
        }
    }

    private enum RefillStage {
        NAVIGATE,
        FILL,
    }
}
