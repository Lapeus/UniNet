package de.dbae.uninet.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dbae.uninet.javaClasses.Emoticon;

/**
 * Dieses Servlet stellt eine Liste der Emoticons zur Verf&uuml;gung und verarbeitet s&auml;tliche daraufbezogene Anfragen.
 * @see Emoticon
 * @author Christian Ackermann
 */
@WebServlet("/EmoticonServlet")
public class EmoticonServlet extends HttpServlet {
	   
	private static final long serialVersionUID = -966983999816024902L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public EmoticonServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Setzt die Emoticon-Listen entsprechend der Spalten, in denen sie spaeter angezeigt werden soll
		request.setAttribute("emoticons1", getEmoticons("Smileys"));
		request.setAttribute("emoticons2", getEmoticons("Liebe"));
		request.setAttribute("emoticons3", getEmoticons("Gestiken"));
		request.setAttribute("emoticons4", getEmoticons("Kleidung"));
		request.setAttribute("emoticons5", getEmoticons("Natur"));
		request.setAttribute("emoticons6", getEmoticons("Tiere"));
		request.setAttribute("emoticons7", getEmoticons("Essen"));
		request.setAttribute("emoticons8", getEmoticons("Verkehr"));
		request.setAttribute("emoticons9", getEmoticons("Sport"));
		request.setAttribute("emoticons10", getEmoticons("Musik"));
		request.setAttribute("emoticons11", getEmoticons("Zeichen und Pfeile"));
		request.setAttribute("emoticons12", getEmoticons("Zeit"));
		request.setAttribute("emoticons13", getEmoticons("Arbeit"));
		request.setAttribute("emoticons14", getEmoticons("Werkzeug"));
		request.setAttribute("emoticons15", getEmoticons("Party"));
		request.setAttribute("emoticons16", getEmoticons("Sonstiges"));
		
		// Lade Chatfreunde
		new LadeChatFreundeServlet().setChatfreunde(request);
		
		// Weiterleitung an die Hilfeseite
		request.getRequestDispatcher("Hilfe.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * Gibt eine Liste aller Emoticons zur&uuml;ck.
	 * @return Eine Liste aller Emoticons
	 */
	public List<Emoticon> getEmoticons() {
		List<Emoticon> emoticons = new ArrayList<Emoticon>();
		emoticons.addAll(getEmoticons("Smileys"));
		emoticons.addAll(getEmoticons("Liebe"));
		emoticons.addAll(getEmoticons("Gestiken"));
		emoticons.addAll(getEmoticons("Kleidung"));
		emoticons.addAll(getEmoticons("Natur"));
		emoticons.addAll(getEmoticons("Tiere"));
		emoticons.addAll(getEmoticons("Essen"));
		emoticons.addAll(getEmoticons("Verkehr"));
		emoticons.addAll(getEmoticons("Sport"));
		emoticons.addAll(getEmoticons("Musik"));
		emoticons.addAll(getEmoticons("Zeichen und Pfeile"));
		emoticons.addAll(getEmoticons("Zeit"));
		emoticons.addAll(getEmoticons("Arbeit"));
		emoticons.addAll(getEmoticons("Werkzeug"));
		emoticons.addAll(getEmoticons("Party"));
		emoticons.addAll(getEmoticons("Sonstiges"));
		return emoticons;
	}
	
	/**
	 * Gibt eine Liste aller Emoticons der angegebenen Kategorie zur&uuml;ck.
	 * @param kategorie Die Kategorie
	 * @return Die Liste der Emoticons der angegebenen Kategorie
	 */
	public List<Emoticon> getEmoticons(String kategorie) {
		// Liste der Emoticons
		List<Emoticon> emoticons = new ArrayList<Emoticon>();
		// Defintion der Farbe 'Hautfarbe'
		String hautfarbe = "#F0E590";
		// In Abhaengigkeit der Kategorie
		switch (kategorie) {
		case "Smileys":
			emoticons.add(new Emoticon("O:)", "&#x1F607"));
			emoticons.add(new Emoticon(":)", "&#x1F60A"));
			emoticons.add(new Emoticon(":D", "&#x1F603"));
			emoticons.add(new Emoticon(";)", "&#x1F609"));
			emoticons.add(new Emoticon("-.-", "&#x1F612"));
			emoticons.add(new Emoticon(":'(", "&#x1F622"));
			emoticons.add(new Emoticon(":O", "&#x1F632"));
			emoticons.add(new Emoticon(":P", "&#x1F61C"));
			emoticons.add(new Emoticon(":*", "&#x1F618"));
			emoticons.add(new Emoticon(":/", "&#x1F629"));
			emoticons.add(new Emoticon("(angry)", "&#x1F620"));
			emoticons.add(new Emoticon("(tired)", "&#x1F62A"));
			emoticons.add(new Emoticon("(no evil monkey)", "&#x1F648"));
			emoticons.add(new Emoticon("(sleeping)", "&#x1F4A4"));
			break;
		case "Liebe":
			emoticons.add(new Emoticon("<3", "<span style='color: #E30000'>&#x2764</span>"));
			emoticons.add(new Emoticon("(heart blue)", "<span style='color: blue'>&#x2764</span>"));
			emoticons.add(new Emoticon("(heart black)", "&#x2764"));
			emoticons.add(new Emoticon("(heart green)", "<span style='color: green'>&#x2764</span>"));
			emoticons.add(new Emoticon("(heart pink)", "<span style='color: #FF80EE'>&#x2764</span>"));
			emoticons.add(new Emoticon("(double heart)", "&#x1F495"));
			emoticons.add(new Emoticon("(broken heart)", "&#x1F494"));
			emoticons.add(new Emoticon("(family)", "&#x1F46A"));
			emoticons.add(new Emoticon("(couple)", "&#x1F46B"));
			emoticons.add(new Emoticon("(love letter)", "&#x1F48C"));
			emoticons.add(new Emoticon("(ring)", "&#x1F48D"));
			emoticons.add(new Emoticon("(diamant)", "&#x1F48E"));
			emoticons.add(new Emoticon("(kiss)", "&#x1F48B"));
			break;
		case "Gestiken":
			emoticons.add(new Emoticon("(stop)", "<span style='color: " + hautfarbe + "'>&#x270B</span>"));
			emoticons.add(new Emoticon("(peace)", "<span style='color: " + hautfarbe + "'>&#x270C</span>"));
			emoticons.add(new Emoticon("(pointing index)", "<span style='color: " + hautfarbe + "'>&#x1F446</span>"));
			emoticons.add(new Emoticon("(fisted hand)", "<span style='color: " + hautfarbe + "'>&#x1F44A</span>"));
			emoticons.add(new Emoticon("(waving)", "<span style='color: " + hautfarbe + "'>&#x1F44B</span>"));
			emoticons.add(new Emoticon("(ok)", "<span style='color: " + hautfarbe + "'>&#x1F44C</span>"));
			emoticons.add(new Emoticon("(y)", "<span style='color: " + hautfarbe + "'>&#x1F44D</span>"));
			emoticons.add(new Emoticon("(n)", "<span style='color: " + hautfarbe + "'>&#x1F44E</span>"));
			emoticons.add(new Emoticon("(paw prints)", "&#x1F43E"));
			emoticons.add(new Emoticon("(eyes)", "&#x1F440"));
			emoticons.add(new Emoticon("(ear)", "&#x1F442"));
			emoticons.add(new Emoticon("(nose)", "&#x1F443"));
			emoticons.add(new Emoticon("(speechbubble)", "&#x1F4AC"));
			emoticons.add(new Emoticon("(thoughtbubble)", "&#x1F4AD"));
			break;
		case "Natur":
			emoticons.add(new Emoticon("(snow)", "<span style='color: #B1C5CC'>&#x2744</span>"));
			emoticons.add(new Emoticon("(snowman)", "&#x26C4"));
			emoticons.add(new Emoticon("(sun)", "<span style='color: #FFF700'>&#x2600</span>"));
			emoticons.add(new Emoticon("(rainbow)", "&#x1F308"));
			emoticons.add(new Emoticon("(stars)", "<span style='color: #FFEF0D'>&#x2728</span>"));
			emoticons.add(new Emoticon("(star)", "<span style='color: #FFEF0D'>&#x2850</span>"));
			emoticons.add(new Emoticon("(cloud)", "<span style='color: #D1DCE3'>&#x2601</span>"));
			emoticons.add(new Emoticon("(umbrella)", "<span style='color: #0984D6'>&#x2614</span>"));
			emoticons.add(new Emoticon("(globe)", "&#x1F30F"));
			emoticons.add(new Emoticon("(wave)", "&#x1F30A"));
			emoticons.add(new Emoticon("(volcano)", "&#x1F30B"));
			emoticons.add(new Emoticon("(moon)", "&#x1F319"));
			emoticons.add(new Emoticon("(shooting star)", "&#x1F320"));
			emoticons.add(new Emoticon("(palm)", "&#x1F334"));
			emoticons.add(new Emoticon("(cactus)", "&#x1F335"));
			emoticons.add(new Emoticon("(tulip)", "&#x1F337"));
			emoticons.add(new Emoticon("(rose)", "&#x1F339"));
			emoticons.add(new Emoticon("(sunflower)", "&#x1F33B"));
			emoticons.add(new Emoticon("(leaf clover)", "&#x1F340"));
			emoticons.add(new Emoticon("(tree)", "&#x1F333"));
			emoticons.add(new Emoticon("(christmas tree)", "&#x1F384"));
			emoticons.add(new Emoticon("(fire)", "&#x1F525"));
			break;
		case "Tiere":
			emoticons.add(new Emoticon("(snail)", "&#x1F40C"));
			emoticons.add(new Emoticon("(snake)", "&#x1F40D"));
			emoticons.add(new Emoticon("(horse)", "&#x1F40E"));
			emoticons.add(new Emoticon("(sheep)", "&#x1F411"));
			emoticons.add(new Emoticon("(monkey)", "&#x1F412"));
			emoticons.add(new Emoticon("(elephant)", "&#x1F418"));
			emoticons.add(new Emoticon("(octopus)", "&#x1F419"));
			emoticons.add(new Emoticon("(ant)", "&#x1F41C"));
			emoticons.add(new Emoticon("(honeybee)", "&#x1F41D"));
			emoticons.add(new Emoticon("(fish)", "&#x1F41F"));
			emoticons.add(new Emoticon("(tropical fish)", "&#x1F420"));
			emoticons.add(new Emoticon("(fishing)", "&#x1F3A3"));
			emoticons.add(new Emoticon("(turtle)", "&#x1F422"));
			emoticons.add(new Emoticon("(hatching chick)", "&#x1F423"));
			emoticons.add(new Emoticon("(baby chick)", "&#x1F424"));
			emoticons.add(new Emoticon("(poodle)", "&#x1F429"));
			emoticons.add(new Emoticon("(camel)", "&#x1F42B"));
			emoticons.add(new Emoticon("(dolphin)", "&#x1F42C"));
			emoticons.add(new Emoticon("(whale)", "&#x1F433"));
			break;
		case "Essen":
			emoticons.add(new Emoticon("(red apple)", "&#x1F34E"));
			emoticons.add(new Emoticon("(green apple)", "&#x1F34F"));
			emoticons.add(new Emoticon("(cherries)", "&#x1F352"));
			emoticons.add(new Emoticon("(strawberry)", "&#x1F353"));
			emoticons.add(new Emoticon("(burger)", "&#x1F354"));
			emoticons.add(new Emoticon("(pizza)", "&#x1F355"));
			emoticons.add(new Emoticon("(bread)", "<span style='color: #C7AE0A'>&#x1F35E</span>"));
			emoticons.add(new Emoticon("(french fries)", "&#x1F35F"));
			emoticons.add(new Emoticon("(doughnut)", "&#x1F369"));
			emoticons.add(new Emoticon("(cookie)", "&#x1F36A"));
			emoticons.add(new Emoticon("(chocolate)", "&#x1F36B"));
			emoticons.add(new Emoticon("(flatware)", "&#x1F374"));
			emoticons.add(new Emoticon("(teacup)", "&#x1F375"));
			emoticons.add(new Emoticon("(wine glass)", "&#x1F377"));
			break;
		case "Musik":
			emoticons.add(new Emoticon("(note)", "&#x1F3B5"));
			emoticons.add(new Emoticon("(notes)", "&#x1F3B6"));
			emoticons.add(new Emoticon("(saxophone)", "&#x1F3B7"));
			emoticons.add(new Emoticon("(guitar)", "&#x1F3B8"));
			emoticons.add(new Emoticon("(keyboard)", "&#x1F3B9"));
			emoticons.add(new Emoticon("(trumpet)", "&#x1F3BA"));
			emoticons.add(new Emoticon("(violin)", "&#x1F3BB"));
			emoticons.add(new Emoticon("(microphone)", "&#x1F3A4"));
			break;
		case "Sport":
			emoticons.add(new Emoticon("(tennis)", "&#x1F3BE"));
			emoticons.add(new Emoticon("(ski)", "&#x1F3BF"));
			emoticons.add(new Emoticon("(basketball)", "&#x1F3C0"));
			emoticons.add(new Emoticon("(chequered flag)", "&#x1F3C1"));
			emoticons.add(new Emoticon("(snowboard)", "&#x1F3C2"));
			emoticons.add(new Emoticon("(runner)", "&#x1F3C3"));
			emoticons.add(new Emoticon("(trophy)", "&#x1F3C6"));
			emoticons.add(new Emoticon("(american football)", "&#x1F3C8"));
			emoticons.add(new Emoticon("(football)", "&#x26BD"));
			emoticons.add(new Emoticon("(baseball)", "&#x26BE"));
			emoticons.add(new Emoticon("(swimmer)", "&#x1F3CA"));
			break;
		case "Verkehr":
			emoticons.add(new Emoticon("(walk)", "&#x1F6B6"));
			emoticons.add(new Emoticon("(wheelchair)", "&#x267F"));
			emoticons.add(new Emoticon("(bus)", "&#x1F68C"));
			emoticons.add(new Emoticon("(bus stop)", "&#x1F68F"));
			emoticons.add(new Emoticon("(taxi)", "&#x1F695"));
			emoticons.add(new Emoticon("(car)", "&#x1F697"));
			emoticons.add(new Emoticon("(jeep)", "&#x1F699"));
			emoticons.add(new Emoticon("(truck)", "&#x1F69A"));
			emoticons.add(new Emoticon("(fuel pump)", "&#x26FD"));
			emoticons.add(new Emoticon("(ship)", "&#x1F6A2"));
			emoticons.add(new Emoticon("(speedboat)", "&#x1F6A4"));
			emoticons.add(new Emoticon("(sailboat)", "&#x26F5"));
			emoticons.add(new Emoticon("(anchor)", "&#x2693"));
			emoticons.add(new Emoticon("(train)", "&#x1F685"));
			emoticons.add(new Emoticon("(rocket)", "&#x1F680"));
			emoticons.add(new Emoticon("(bicycle)", "&#x1F6B2"));
			break;
		case "Kleidung":
			emoticons.add(new Emoticon("(eyeglasses)", "&#x1F453"));
			emoticons.add(new Emoticon("(t-shirt)", "&#x1F455"));
			emoticons.add(new Emoticon("(jeans)", "&#x1F456"));
			emoticons.add(new Emoticon("(dress)", "&#x1F457"));
			emoticons.add(new Emoticon("(bikini)", "&#x1F459"));
			emoticons.add(new Emoticon("(handbag)", "&#x1F45C"));
			emoticons.add(new Emoticon("(shoe)", "&#x1F45E"));
			emoticons.add(new Emoticon("(sportshoe)", "&#x1F45F"));
			emoticons.add(new Emoticon("(highheel)", "&#x1F460"));
			emoticons.add(new Emoticon("(footprints)", "&#x1F463"));
			break;
		case "Zeichen und Pfeile":
			emoticons.add(new Emoticon("(c)", "&#x00A9"));
			emoticons.add(new Emoticon("(r)", "&#x00AE"));
			emoticons.add(new Emoticon("<->", "&#x2194"));
			emoticons.add(new Emoticon("->", "&#x27A1"));
			emoticons.add(new Emoticon("<-", "&#x2B05"));
			emoticons.add(new Emoticon(">>", "&#x23E9"));
			emoticons.add(new Emoticon("<<", "&#x23EA"));
			emoticons.add(new Emoticon("(smoke)", "&#x1F6AC"));
			emoticons.add(new Emoticon("(dont smoke)", "&#x1F6AD"));
			emoticons.add(new Emoticon("(dont)", "&#x1F6AB"));
			emoticons.add(new Emoticon("(check)", "<span style='color: #00AB03'>&#x2714</span>"));
			emoticons.add(new Emoticon("(cross)", "<span style='color: #ED0000'>&#x2716</span>"));
			break;
		case "Zeit":
			emoticons.add(new Emoticon("(alarm clock)", "&#x23F0"));
			emoticons.add(new Emoticon("(hourglass)", "&#x23F3"));
			emoticons.add(new Emoticon("(clock0)", "&#x1F55B"));
			emoticons.add(new Emoticon("(clock1)", "&#x1F550"));
			emoticons.add(new Emoticon("(clock2)", "&#x1F551"));
			emoticons.add(new Emoticon("(clock3)", "&#x1F552"));
			emoticons.add(new Emoticon("(clock4)", "&#x1F553"));
			emoticons.add(new Emoticon("(clock5)", "&#x1F554"));
			emoticons.add(new Emoticon("(clock6)", "&#x1F555"));
			emoticons.add(new Emoticon("(clock7)", "&#x1F556"));
			emoticons.add(new Emoticon("(clock8)", "&#x1F557"));
			emoticons.add(new Emoticon("(clock9)", "&#x1F558"));
			emoticons.add(new Emoticon("(clock10)", "&#x1F559"));
			emoticons.add(new Emoticon("(clock11)", "&#x1F55A"));
			break;
		case "Arbeit":
			emoticons.add(new Emoticon("(letter)", "&#x2709"));
			emoticons.add(new Emoticon("(telephone)", "&#x260E"));
			emoticons.add(new Emoticon("(headphone)", "&#x1F3A7"));
			emoticons.add(new Emoticon("(floppy disk)", "&#x1F4BE"));
			emoticons.add(new Emoticon("(pushpin)", "&#x1F4CC"));
			emoticons.add(new Emoticon("(paperclip)", "&#x1F4CE"));
			emoticons.add(new Emoticon("(straight ruler)", "&#x1F4CF"));
			emoticons.add(new Emoticon("(traingular ruler)", "&#x1F4D0"));
			emoticons.add(new Emoticon("(book)", "&#x1F4D6"));
			emoticons.add(new Emoticon("(books)", "&#x1F4DA"));
			break;
		case "Party":
			emoticons.add(new Emoticon("(present)", "&#x1F381"));
			emoticons.add(new Emoticon("(cake)", "&#x1F382"));
			emoticons.add(new Emoticon("(balloon)", "&#x1F388"));
			emoticons.add(new Emoticon("(party)", "&#x1F389"));
			emoticons.add(new Emoticon("(confetti)", "&#x1F38A"));
			break;
		case "Werkzeug":
			emoticons.add(new Emoticon("(wrench)", "&#x1F527"));
			emoticons.add(new Emoticon("(hammer)", "&#x1F528"));
			emoticons.add(new Emoticon("(knife)", "&#x1F52A"));
			emoticons.add(new Emoticon("(light bulb)", "&#x1F4A1"));
			emoticons.add(new Emoticon("(bomb)", "&#x1F4A3"));
			emoticons.add(new Emoticon("(zoom)", "&#x1F50D"));
			emoticons.add(new Emoticon("(key)", "&#x1F511"));
			emoticons.add(new Emoticon("(lock)", "&#x1F512"));
			emoticons.add(new Emoticon("(open lock)", "&#x1F513"));
			break;
		case "Sonstiges":
			emoticons.add(new Emoticon("(uLogo)", "<img style='margin-left: 4px; margin-right: 4px; width: 16px; height: 16px;'"
					+ " alt='(uninet)' src='UniNet_Logo.ico'>"));
			emoticons.add(new Emoticon("(uninet)", "<span class='facebook'>r<span class='facebook2'>UNINET</span>r</span>"));
			emoticons.add(new Emoticon("(facebook)", "<span class='facebook'>r<span class='facebook2'>facebook</span>r</span>"));
			emoticons.add(new Emoticon("(door)", "&#x1F6AA"));
			emoticons.add(new Emoticon("(castle)", "&#x1F3F0"));
			emoticons.add(new Emoticon("(camera)", "&#x1F3A5"));
			emoticons.add(new Emoticon("(tv)", "&#x1F4FA"));
			emoticons.add(new Emoticon("(poo)", "&#x1F4A9"));
			emoticons.add(new Emoticon("(money)", "&#x1F4B0"));
			emoticons.add(new Emoticon("(dollar)", "&#x1F4B2"));
			emoticons.add(new Emoticon("(bell)", "&#x1F514"));
			break;
		default:
			break;
		}
		return emoticons;
	}

}