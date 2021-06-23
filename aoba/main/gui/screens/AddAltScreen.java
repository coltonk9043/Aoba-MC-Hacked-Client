package aoba.main.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;

import aoba.main.altmanager.Alt;
import aoba.main.gui.widgets.PasswordFieldWidget;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AddAltScreen extends Screen {
	   private Button buttonAddAlt;
	   private final BooleanConsumer booleanConsumer;
	   private PasswordFieldWidget textFieldAltPassword;
	   private TextFieldWidget textFieldAltUsername;
	   private final AltScreen parent;
	   private Alt alt;
	   
	   public AddAltScreen(AltScreen parent,BooleanConsumer bool, Alt alt) {
	      super(new TranslationTextComponent("Add Alt"));
	      this.parent = parent;
	      this.booleanConsumer = bool;
	      this.alt = alt;
	   }

	public void tick() {
	      this.textFieldAltUsername.tick();
	      this.textFieldAltPassword.tick();
	   }

	   protected void init() {
	      this.minecraft.keyboardListener.enableRepeatEvents(true);
	      this.textFieldAltUsername = new TextFieldWidget(this.font, this.width / 2 - 100, 166, 200, 20, new TranslationTextComponent("Enter Name"));
	      this.textFieldAltUsername.setFocused2(true);
	      
	      this.textFieldAltUsername.setText(this.alt == null ? "" : alt.getEmail());
	      this.textFieldAltUsername.setResponder(this::func_213028_a);
	      this.children.add(this.textFieldAltUsername);
	      this.textFieldAltPassword = new PasswordFieldWidget(this.font, this.width / 2 - 100, 206, 200, 20, new TranslationTextComponent("Enter Password"));
	      this.textFieldAltPassword.setMaxStringLength(128);
	      this.textFieldAltPassword.setText(this.alt == null ? "" : alt.getPassword());
	      this.textFieldAltPassword.setResponder(this::func_213028_a);
	      this.children.add(this.textFieldAltPassword);
	   
	      this.buttonAddAlt = this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 96 + 18, 200, 20, new TranslationTextComponent(this.alt == null ? "Add Alt" : "Save Alt"), (p_213030_1_) -> {
	         this.onButtonAltAddPressed();
	      }));
	      this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 20, new TranslationTextComponent("Cancel"), (p_213029_1_) -> {
	         this.booleanConsumer.accept(false);
	      }));
	      this.func_228180_b_();
	   }

	   public void resize(Minecraft p_resize_1_, int p_resize_2_, int p_resize_3_) {
	      String s = this.textFieldAltPassword.getText();
	      String s1 = this.textFieldAltUsername.getText();
	      this.init(p_resize_1_, p_resize_2_, p_resize_3_);
	      this.textFieldAltPassword.setText(s);
	      this.textFieldAltUsername.setText(s1);
	   }

	   private void func_213028_a(String p_213028_1_) {
	      this.func_228180_b_();
	   }

	   public void removed() {
	      this.minecraft.keyboardListener.enableRepeatEvents(false);
	   }

	   private void onButtonAltAddPressed() {
		   if(this.alt == null) {
			   Alt alt = new Alt(this.textFieldAltUsername.getText(), this.textFieldAltPassword.getText());
				  Minecraft.getInstance().aoba.am.addAlt(alt);
			      this.booleanConsumer.accept(true);
			      this.parent.refreshAltList();
		   }else {
			   alt.setEmail(this.textFieldAltUsername.getText());
			   alt.setPassword(this.textFieldAltPassword.getText());
			   Minecraft.getInstance().aoba.am.saveAlts();
			   this.booleanConsumer.accept(true);
			   this.parent.refreshAltList();
		   }
	   }

	   public void onClose() {
	      this.func_228180_b_();
	   }

	   private void func_228180_b_() {
	      String s = this.textFieldAltUsername.getText();
	      boolean flag = !s.isEmpty() && s.split(":").length > 0 && s.indexOf(32) == -1;
	      this.buttonAddAlt.active = flag && !this.textFieldAltUsername.getText().isEmpty();
	   }

	   public void render(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
	      this.renderBackground(matrixStack);
	      drawCenteredString(matrixStack, this.font, this.title.getString(), this.width / 2, 17, 16777215);
	      drawString(matrixStack, this.font, this.textFieldAltPassword.getText().isEmpty() ? I18n.format("Cracked (Poverty) Account") : I18n.format("Premium Account"), this.width / 2 - 100, 138, this.textFieldAltPassword.getText().isEmpty() ? 0xFF0000 : 0x00FF00);
	      drawString(matrixStack, this.font, I18n.format("Enter Username"), this.width / 2 - 100, 153, 10526880);
	      drawString(matrixStack, this.font, I18n.format("Enter Password"), this.width / 2 - 100, 194, 10526880);
	      this.textFieldAltUsername.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
	      this.textFieldAltPassword.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
	      super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
	   }
	}
