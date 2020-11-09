package com.unimi.apiserver.resource;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class UserProfile {
	private String name;
	private String email;

	// Ordini tra cui l'operatore può scegliere
	private List<Ordine> ordiniDisponibili;

	public UserProfile() {
		// Ordini mockati
		ordiniDisponibili = new ArrayList<>();
		Ordine ord1 = new Ordine("19A", 'A');
		Ordine ord2 = new Ordine("3B", 'A');
		Ordine ord3 = new Ordine("15A", 'C');
		ordiniDisponibili.add(ord1);
		ordiniDisponibili.add(ord2);
		ordiniDisponibili.add(ord3);
	}
}
