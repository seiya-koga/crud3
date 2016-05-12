package controllers;

import java.sql.Date;
import play.db.ebean.Model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import play.*;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.*;

import views.html.*;
import models.Message;
import static play.data.Form.form;

public class Application extends Controller {
	final static Form<Message> messageForm = Form.form(Message.class);

	public static Result index() {
		List<Message> messages = Message.find.all();

		return ok(index.render(messages));

	}

	// add
	public static Result add() {
		return ok(add.render(messageForm));
	}

	public static Result doAdd() {
		Form<Message> addForm = messageForm.bindFromRequest();
		if (!addForm.hasErrors()) {
			addForm.get().postdate = new Date(System.currentTimeMillis());
			addForm.get().save();
			return redirect(routes.Application.index());
		}
		return ok(add.render(addForm));
	}

	// delete
	public static Result delete() {
		return ok(delete.render("ID番号を入力してください。", messageForm));
	}

	public static Result doDelete() {
		Form<Message> deleteForm = messageForm.bindFromRequest();
		if (!deleteForm.hasErrors()) {
			if (deleteForm.get().id != null) {
				Message message = Message.find.byId(deleteForm.get().id);
				if (message != null) {
					message.delete();
					return redirect(routes.Application.index());
				}
			}
		}
		return ok(delete.render("ERROR:IDの投稿が見つかりません", messageForm));
	}

	// setItem
	public static Result setItem() {
		return ok(setItem.render("投稿メッセージID検索画面", "ID番号を入力", messageForm));
	}

	// edit
	public static Result edit() {
		String msg = "";
		Form<Message> editForm = messageForm.bindFromRequest();
		if (!editForm.hasErrors()) {
			if (editForm.get().id != null) {
				Message message = Message.find.byId(editForm.get().id);
				if (message != null) {
					editForm = editForm.fill(message);
					msg = "投稿メッセージ編集画面";
					return ok(edit.render(msg, "ID=" + message.id + "の投稿を編集", editForm, editForm.get().id));
				} else {
					msg = "ERROR:IDの投稿が見つかりません";
				}
			}

		}
		return ok(setItem.render("投稿メッセージID検索画面", msg, messageForm));
	}

	public static Result doEdit() {
		Form<Message> editForm = messageForm.bindFromRequest();
		Long id = null;
		try {
			id = Long.parseLong(request().body().asFormUrlEncoded().get("id")[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (id != null) {
			Message message = Message.find.byId(id);
			editForm = editForm.fill(message).bindFromRequest();
			if (!editForm.hasErrors()) {
				editForm.get().update();
				return redirect(routes.Application.index());
			}
		}
		return ok(edit.render("投稿メッセージ編集画面", "ERROR", editForm, id));
	}

	// find
	public static Result find() {
		List<Message> messages = null;
		return ok(find.render(messages, messageForm));
	}

	public static Result doFind() {
		Form<Message> findForm = messageForm.bindFromRequest();
		List<Message> messages = null;
		if (findForm.hasErrors()) {
			return ok(find.render(messages, findForm));
		} else {
			if (findForm.get().name != null) {
				messages = Message.find.where().eq("name", findForm.get().name).findList();
			}
			return ok(find.render(messages, findForm));
		}
	}
}
