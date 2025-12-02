package br.com.pereiraeng.html;

import java.awt.Color;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;

import br.com.pereiraeng.core.ColorUtils;
import br.com.pereiraeng.core.StringUtils;
import br.com.pereiraeng.core.collections.ListUtils;

public class HTML {

	public static final String START = "<!DOCTYPE html>";

	private static final String HEAD = START + "\n<html>\n<head>\n<title>%s</title>\n%s</head>\n<body%s>\n";

	private static final String HEAD_STYLE = "<style>\n%s</style>\n";

	public static final String TAIL = "</body>\n</html>\n";

	/**
	 * A blank html
	 */
	public static final String BLANK = String.format(HEAD, "", "", "") + TAIL;

	/**
	 * Função que retorna a sequência de caracteres que é o cabeçalho dos documentos
	 * HTML
	 * 
	 * @return cabeçalho HTML
	 */
	public static String getHead() {
		return getHead("", "");
	}

	/**
	 * Função que retorna a sequência de caracteres que é o cabeçalho dos documentos
	 * HTML
	 * 
	 * @param style           estilo geral
	 * @param bodyAttrib atributos do body (e.g., style)
	 * @return cabeçalho HTML
	 */
	public static String getHead(String style, String bodyAttrib) {
		return getHead("", style, bodyAttrib);
	}

	/**
	 * Função que retorna a sequência de caracteres que é o cabeçalho dos documentos
	 * HTML
	 * 
	 * @param title           título
	 * @param style           estilo geral
	 * @param bodyAttrib atributos do body (e.g., style)
	 * @return cabeçalho HTML
	 */
	public static String getHead(String title, String style, String bodyAttrib) {
		return String.format(HEAD, title, style.length() == 0 ? "" : String.format(HEAD_STYLE, style),
				bodyAttrib.length() == 0 ? "" : (" " + bodyAttrib));
	}

	public static final String COMMENT_OPENING_TAG = "<!--";

	public static final String COMMENT_CLOSING_TAG = "-->";

	public static final Pattern PATTERN_COMMENT_OPENING_TAG = Pattern.compile(COMMENT_OPENING_TAG);

	public static final Pattern PATTERN_COMMENT_CLOSING_TAG = Pattern.compile(COMMENT_CLOSING_TAG);

	// -------------------- HTML GROUPS --------------------

	public static final String ATTRIB_VALUE = "[\\p{Alnum} -:;,\\(\\)_]+";

	public static final String ATTRIB = "(style|id|disabled|title|href|src|alt|width|height|value)=\"" + ATTRIB_VALUE
			+ "\"";

	private static final String GROUP = "<%1$s(| " + ATTRIB + ")>.+?</%1$s>";

	public static final String[] GROUPS = { "big", "span", "sub", "sup" };

	public static Pattern getGroupPattern(String name) {
		return Pattern.compile(String.format(GROUP, name), Pattern.DOTALL);
	}

	/**
	 * Padrão do interior xml
	 */
	public static final Pattern PATTERN_CONTENT = Pattern.compile(">.*<", Pattern.DOTALL);

	private static final String INNER_CONTENT = "((?!.*<%2$s).*?)";

	// span

	private static final String FORMATTER = "<%1$s(| " + ATTRIB + ")>" + INNER_CONTENT + "</%1$s>";

	public static Pattern getGroupFormatterPattern(String groupName) {
		return Pattern.compile(String.format(FORMATTER, groupName, "(" + StringUtils.addSeparator(GROUPS, "|") + ")"),
				Pattern.DOTALL);
	}

	// sub e sup

	public static final Pattern PATTERN_SUB = getGroupPattern("sub"), PATTERN_SUP = getGroupPattern("sup");

	// link

	public static final Pattern PATTERN_LINK = Pattern.compile("<(a|A) ((href)|(HREF))=\".+?\">.+?</(a|A)>");

	// --------------------- LINKS HTML -----------------------

	private static final String LINK_OPENING_TAG = "<a href=\"%s\">";

	public static final String LINK = LINK_OPENING_TAG + "%s</a>";

	/**
	 * Função que retorna o {@link Pattern padrão} para localização de links em
	 * textos HTML
	 * 
	 * @param url   endereço para o qual este link aponta
	 * @param link etiqueta do link (se este argumento for <code>null</code>, então
	 *                         só a primeira parte do link entrará no padrão)
	 * @return {@link Pattern padrão} para localização dos links
	 */
	public static Pattern getLinkPattern(String url, String link) {
		String pattern = null;
		if (link != null)
			pattern = String.format(LINK, url, link);
		else
			pattern = String.format(LINK_OPENING_TAG, url);
		return Pattern.compile(pattern);
	}

	// -------------------- HTML CARACTERES ESPECIAIS --------------------

	public static final Pattern PATTERN_UNICODE = Pattern.compile("&#([xX]\\p{XDigit}+|\\d+);"),
			PATTERN_SPECIAL = Pattern.compile("&\\p{Alnum}+;");

	private static final Map<String, Integer> HTML_SPE_CHAR;

	static {
		Map<String, Integer> map = new HashMap<>();
		// -----------------------------------------------------
		map.put("quot", 34);
		map.put("amp", 38);
		map.put("apos", 39);
		map.put("lt", 60);
		map.put("gt", 62);
		map.put("nbsp", 160);
		map.put("iexcl", 161);
		map.put("cent", 162);
		map.put("pound", 163);
		map.put("curren", 164);
		map.put("yen", 165);
		map.put("brvbar", 166);
		map.put("sect", 167);
		map.put("uml", 168);
		map.put("copy", 169);
		map.put("ordf", 170);
		map.put("laquo", 171);
		map.put("not", 172);
		map.put("shy", 173);
		map.put("reg", 174);
		map.put("macr", 175);
		map.put("deg", 176);
		map.put("plusmn", 177);
		map.put("sup2", 178);
		map.put("sup3", 179);
		map.put("acute", 180);
		map.put("micro", 181);
		map.put("para", 182);
		map.put("middot", 183);
		map.put("cedil", 184);
		map.put("sup1", 185);
		map.put("ordm", 186);
		map.put("raquo", 187);
		map.put("frac14", 188);
		map.put("frac12", 189);
		map.put("frac34", 190);
		map.put("iquest", 191);
		map.put("Agrave", 192);
		map.put("Aacute", 193);
		map.put("Acirc", 194);
		map.put("Atilde", 195);
		map.put("Auml", 196);
		map.put("Aring", 197);
		map.put("AElig", 198);
		map.put("Ccedil", 199);
		map.put("Egrave", 200);
		map.put("Eacute", 201);
		map.put("Ecirc", 202);
		map.put("Euml", 203);
		map.put("Igrave", 204);
		map.put("Iacute", 205);
		map.put("Icirc", 206);
		map.put("Iuml", 207);
		map.put("ETH", 208);
		map.put("Ntilde", 209);
		map.put("Ograve", 210);
		map.put("Oacute", 211);
		map.put("Ocirc", 212);
		map.put("Otilde", 213);
		map.put("Ouml", 214);
		map.put("times", 215);
		map.put("Oslash", 216);
		map.put("Ugrave", 217);
		map.put("Uacute", 218);
		map.put("Ucirc", 219);
		map.put("Uuml", 220);
		map.put("Yacute", 221);
		map.put("THORN", 222);
		map.put("szlig", 223);
		map.put("agrave", 224);
		map.put("aacute", 225);
		map.put("acirc", 226);
		map.put("atilde", 227);
		map.put("auml", 228);
		map.put("aring", 229);
		map.put("aelig", 230);
		map.put("ccedil", 231);
		map.put("egrave", 232);
		map.put("eacute", 233);
		map.put("ecirc", 234);
		map.put("euml", 235);
		map.put("igrave", 236);
		map.put("iacute", 237);
		map.put("icirc", 238);
		map.put("iuml", 239);
		map.put("eth", 240);
		map.put("ntilde", 241);
		map.put("ograve", 242);
		map.put("oacute", 243);
		map.put("ocirc", 244);
		map.put("otilde", 245);
		map.put("ouml", 246);
		map.put("divide", 247);
		map.put("oslash", 248);
		map.put("ugrave", 249);
		map.put("uacute", 250);
		map.put("ucirc", 251);
		map.put("uuml", 252);
		map.put("yacute", 253);
		map.put("thorn", 254);
		map.put("yuml", 255);
		map.put("OElig", 338);
		map.put("oelig", 339);
		map.put("Scaron", 352);
		map.put("scaron", 353);
		map.put("Yuml", 376);
		map.put("fnof", 402);
		map.put("circ", 710);
		map.put("tilde", 732);
		map.put("Alpha", 913);
		map.put("Beta", 914);
		map.put("Gamma", 915);
		map.put("Delta", 916);
		map.put("Epsilon", 917);
		map.put("Zeta", 918);
		map.put("Eta", 919);
		map.put("Theta", 920);
		map.put("Iota", 921);
		map.put("Kappa", 922);
		map.put("Lambda", 923);
		map.put("Mu", 924);
		map.put("Nu", 925);
		map.put("Xi", 926);
		map.put("Omicron", 927);
		map.put("Pi", 928);
		map.put("Rho", 929);
		map.put("Sigma", 931);
		map.put("Tau", 932);
		map.put("Upsilon", 933);
		map.put("Phi", 934);
		map.put("Chi", 935);
		map.put("Psi", 936);
		map.put("Omega", 937);
		map.put("alpha", 945);
		map.put("beta", 946);
		map.put("gamma", 947);
		map.put("delta", 948);
		map.put("epsilon", 949);
		map.put("zeta", 950);
		map.put("eta", 951);
		map.put("theta", 952);
		map.put("iota", 953);
		map.put("kappa", 954);
		map.put("lambda", 955);
		map.put("mu", 956);
		map.put("nu", 957);
		map.put("xi", 958);
		map.put("omicron", 959);
		map.put("pi", 960);
		map.put("rho", 961);
		map.put("sigmaf", 962);
		map.put("sigma", 963);
		map.put("tau", 964);
		map.put("upsilon", 965);
		map.put("phi", 966);
		map.put("chi", 967);
		map.put("psi", 968);
		map.put("omega", 969);
		map.put("thetasym", 977);
		map.put("upsih", 978);
		map.put("piv", 982);
		map.put("ensp", 8194);
		map.put("emsp", 8195);
		map.put("thinsp", 8201);
		map.put("zwnj", 8204);
		map.put("zwj", 8205);
		map.put("lrm", 8206);
		map.put("rlm", 8207);
		map.put("ndash", 8211);
		map.put("mdash", 8212);
		map.put("lsquo", 8216);
		map.put("rsquo", 8217);
		map.put("sbquo", 8218);
		map.put("ldquo", 8220);
		map.put("rdquo", 8221);
		map.put("bdquo", 8222);
		map.put("dagger", 8224);
		map.put("Dagger", 8225);
		map.put("bull", 8226);
		map.put("hellip", 8230);
		map.put("permil", 8240);
		map.put("prime", 8242);
		map.put("Prime", 8243);
		map.put("lsaquo", 8249);
		map.put("rsaquo", 8250);
		map.put("oline", 8254);
		map.put("frasl", 8260);
		map.put("euro", 8364);
		map.put("image", 8465);
		map.put("weierp", 8472);
		map.put("real", 8476);
		map.put("trade", 8482);
		map.put("alefsym", 8501);
		map.put("larr", 8592);
		map.put("uarr", 8593);
		map.put("rarr", 8594);
		map.put("darr", 8595);
		map.put("harr", 8596);
		map.put("crarr", 8629);
		map.put("lArr", 8656);
		map.put("uArr", 8657);
		map.put("rArr", 8658);
		map.put("dArr", 8659);
		map.put("hArr", 8660);
		map.put("forall", 8704);
		map.put("part", 8706);
		map.put("exist", 8707);
		map.put("empty", 8709);
		map.put("nabla", 8711);
		map.put("isin", 8712);
		map.put("notin", 8713);
		map.put("ni", 8715);
		map.put("prod", 8719);
		map.put("sum", 8721);
		map.put("minus", 8722);
		map.put("lowast", 8727);
		map.put("radic", 8730);
		map.put("prop", 8733);
		map.put("infin", 8734);
		map.put("ang", 8736);
		map.put("and", 8743);
		map.put("or", 8744);
		map.put("cap", 8745);
		map.put("cup", 8746);
		map.put("int", 8747);
		map.put("there4", 8756);
		map.put("sim", 8764);
		map.put("cong", 8773);
		map.put("asymp", 8776);
		map.put("ne", 8800);
		map.put("equiv", 8801);
		map.put("le", 8804);
		map.put("ge", 8805);
		map.put("sub", 8834);
		map.put("sup", 8835);
		map.put("nsub", 8836);
		map.put("sube", 8838);
		map.put("supe", 8839);
		map.put("oplus", 8853);
		map.put("otimes", 8855);
		map.put("perp", 8869);
		map.put("sdot", 8901);
		map.put("lceil", 8968);
		map.put("rceil", 8969);
		map.put("lfloor", 8970);
		map.put("rfloor", 8971);
		map.put("lang", 9001);
		map.put("rang", 9002);
		map.put("loz", 9674);
		map.put("spades", 9824);
		map.put("clubs", 9827);
		map.put("hearts", 9829);
		map.put("diams", 9830);
		// -----------------------------------------------------
		HTML_SPE_CHAR = Collections.unmodifiableMap(map);
	}

	private static String replaceName(String name) {
		int c = replaceNameUnicode(name);
		if (c >= 0)
			return String.format("%c", c);
		else
			return null;
	}

	/**
	 * Função que procura na {@link HTML#HTML_SPE_CHAR tabela} de nomes HTML o
	 * código Unicode equivalente
	 * 
	 * @param name nome do símbolo HTML
	 * @return inteiro que designa o código UNICODE equivalente
	 */
	public static int replaceNameUnicode(String name) {
		Integer out = HTML_SPE_CHAR.get(name);
		if (out == null)
			return -1;
		else
			return out;
	}

	/**
	 * <p>
	 * Função que converte códigos HTML na forma '&#(x[hexadecimal]|[decimal]);',
	 * onde o número é aquele do código UNICODE, ou '&#[name];', onde o nome é
	 * aquele definido pelo código HTML, em seus respectivos caracteres com acentos
	 * e outros sinais distintivos.
	 * </p>
	 * Se deseja-se converter somente os acentos do texto,
	 * {@link HTML#convertHTMLaccent(String)}.
	 * 
	 * 
	 * @param html texto em HTML
	 * @return texto normal
	 */
	public static String convertHTMLSpeChar(String html) {
		// agora os caracteres especiais na forma &#[número];
		Matcher m = PATTERN_UNICODE.matcher(html);
		StringBuffer sb = new StringBuffer(html.length());
		while (m.find()) {
			String s = m.group();
			s = s.substring(2, s.length() - 1);
			if (Character.toLowerCase(s.charAt(0)) == 'x')
				s = String.format("%c", (char) Integer.parseInt(s.substring(1), 16));
			else
				s = String.format("%c", (char) Integer.parseInt(s));
			m.appendReplacement(sb, Matcher.quoteReplacement(s));
		}
		m.appendTail(sb);
		html = sb.toString();

		// agora os caracteres especiais na forma &[letras];
		m = PATTERN_SPECIAL.matcher(html);
		sb = new StringBuffer(html.length());
		while (m.find()) {
			String s = m.group();
			s = replaceName(s.substring(1, s.length() - 1));
			m.appendReplacement(sb, Matcher.quoteReplacement(s));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	// -------------------- HTML ACENTOS --------------------

	/**
	 * Função que substitui as letras acentuadas de um texto pelos correspondentes
	 * códigos HTML
	 * 
	 * @param text texto a ser convertido para HTML
	 * @return texto convertido
	 */
	public static String accent2HTML(String text) {
		StringBuilder out = new StringBuilder();
		String[] ss = text.split("");
		for (String s : ss) {
			s = Normalizer.normalize(s, Normalizer.Form.NFD);
			if (s.length() == 2)
				out.append(String.format("&%c%s;", s.charAt(0), getHTMLAccentName(s.charAt(1))));
			else
				out.append(s);
		}
		return out.toString();
	}

	public static String accent2HTML2(String text) {
		StringBuilder out = new StringBuilder();
		String[] ss = text.split("");
		for (String s : ss) {
			s = Normalizer.normalize(s, Normalizer.Form.NFD);
			if (s.length() == 2)
				out.append(String.format("&#x%04X;",
						HTML_SPE_CHAR.get(String.format("%c%s", s.charAt(0), getHTMLAccentName(s.charAt(1))))));
			else
				out.append(s);
		}
		return out.toString();
	}

	/**
	 * diacritic
	 */
	private static final Pattern PATTERN_HTML_DIAC = Pattern.compile("&.(acute|grave|circ|tilde|uml|cedil);");

	/**
	 * Função que converte códigos HTML em um texto com a acentuação nomal. Essa
	 * função não altera os demais códigos HTML relativos a caracteres especiais
	 * (para isso, ver {@link HTML#convertHTMLSpeChar(String)}).
	 * 
	 * @param html texto em HTML
	 * @return texto com acentuação normal
	 */
	public static String convertHTMLaccent(String html) {
		Matcher m = PATTERN_HTML_DIAC.matcher(html);
		StringBuffer sb = new StringBuffer(html.length());
		while (m.find()) {
			String s = m.group();
			s = Normalizer.normalize(String.format("%c%c", s.charAt(1), getAccent(s.substring(2, s.length() - 1))),
					Form.NFC);
			m.appendReplacement(sb, Matcher.quoteReplacement(s));
		}
		m.appendTail(sb);

		return sb.toString();
	}

	/**
	 * Função que retorna o nome do acento de acordo com o código HTML. É a função
	 * inversa de {@link HTML#getAccent(String)}
	 * 
	 * @param accent caracter relativo ao acento
	 * @return <code>String</code> com o nome do acento
	 */
	private static String getHTMLAccentName(char accent) {
		switch (accent) {
		case '\u0300':
			return "grave";
		case '\u0301':
			return "acute";
		case '\u0302':
			return "circ";
		case '\u0303':
			return "tilde";
		case '\u0308':
			return "uml";
		case '\u0327':
			return "cedil";
		default:
			System.err.println("Acento não reconhecido: " + accent + " " + String.format(" \\u%04x", accent + 0));
			return null;
		}
	}

	/**
	 * Função que retorna o acento a partir do código HTML. É a função inversa de
	 * {@link HTML#getHTMLAccentName(char)}
	 * 
	 * @param name <code>String</code> com o nome do acento
	 * @return caracter relativo ao acento
	 */
	private static char getAccent(String name) {
		switch (name) {
		case "grave":
			return '\u0300';
		case "acute":
			return '\u0301';
		case "circ":
			return '\u0302';
		case "tilde":
			return '\u0303';
		case "uml":
			return '\u0308';
		case "cedil":
			return '\u0327';
		default:
			return ' ';
		}
	}

	// ============================== COLOR ==============================

	/**
	 * Função que converte uma cor numa sequência de caracteres que a identifica na
	 * linguagem de marcação HTML. É a função inversa de
	 * {@link #html2color(String)}.
	 * 
	 * @param c cor
	 * @return sequência de caracteres que designa a cor (seja o nome dela, seja na
	 *         forma rgb)
	 */
	public static String color2html(Color c) {
		String out = rgb2html(ColorUtils.color2rgb(c));
		if (out == null)
			out = ColorUtils.color2html(c);
		return out;
	}

	/**
	 * Função que converte um número inteiro que representa uma cor numa sequência
	 * de caracteres que a identifica na linguagem de marcação HTML. É a função
	 * inversa de {@link #html2rgb(String)}.
	 * 
	 * @param rgb inteiro cujo valor varia entre 0 (preto) e 16777215 (branco,
	 *            0xFFFFFF)
	 * @return nome da cor, tal como aceita na linguagem de marcação HTML
	 */
	private static String rgb2html(int rgb) {
		switch (rgb) {
		case 0x000000:
			return "Black";
		case 0x000080:
			return "Navy";
		case 0x00008B:
			return "DarkBlue";
		case 0x0000CD:
			return "MediumBlue";
		case 0x0000FF:
			return "Blue";
		case 0x006400:
			return "DarkGreen";
		case 0x008000:
			return "Green";
		case 0x008080:
			return "Teal";
		case 0x008B8B:
			return "DarkCyan";
		case 0x00BFFF:
			return "DeepSkyBlue";
		case 0x00CED1:
			return "DarkTurquoise";
		case 0x00FA9A:
			return "MediumSpringGreen";
		case 0x00FF00:
			return "Lime";
		case 0x00FF7F:
			return "SpringGreen";
		case 0x00FFFF:
			return "Cyan";
		// return "Aqua";
		case 0x191970:
			return "MidnightBlue";
		case 0x1E90FF:
			return "DodgerBlue";
		case 0x20B2AA:
			return "LightSeaGreen";
		case 0x228B22:
			return "ForestGreen";
		case 0x2E8B57:
			return "SeaGreen";
		case 0x2F4F4F:
			return "DarkSlateGray";
		// return "DarkSlateGrey";
		case 0x32CD32:
			return "LimeGreen";
		case 0x3CB371:
			return "MediumSeaGreen";
		case 0x40E0D0:
			return "Turquoise";
		case 0x4169E1:
			return "RoyalBlue";
		case 0x4682B4:
			return "SteelBlue";
		case 0x483D8B:
			return "DarkSlateBlue";
		case 0x48D1CC:
			return "MediumTurquoise";
		case 0x4B0082:
			return "Indigo";
		case 0x556B2F:
			return "DarkOliveGreen";
		case 0x5F9EA0:
			return "CadetBlue";
		case 0x6495ED:
			return "CornflowerBlue";
		case 0x663399:
			return "RebeccaPurple";
		case 0x66CDAA:
			return "MediumAquaMarine";
		case 0x696969:
			return "DimGray";
		// return "DimGrey";
		case 0x6A5ACD:
			return "SlateBlue";
		case 0x6B8E23:
			return "OliveDrab";
		case 0x708090:
			return "SlateGray";
		// return "SlateGrey";
		case 0x778899:
			return "LightSlateGray";
		// return "LightSlateGrey";
		case 0x7B68EE:
			return "MediumSlateBlue";
		case 0x7CFC00:
			return "LawnGreen";
		case 0x7FFF00:
			return "Chartreuse";
		case 0x7FFFD4:
			return "Aquamarine";
		case 0x800000:
			return "Maroon";
		case 0x800080:
			return "Purple";
		case 0x808000:
			return "Olive";
		case 0x808080:
			return "Gray";
		// return "Grey";
		case 0x87CEEB:
			return "SkyBlue";
		case 0x87CEFA:
			return "LightSkyBlue";
		case 0x8A2BE2:
			return "BlueViolet";
		case 0x8B0000:
			return "DarkRed";
		case 0x8B008B:
			return "DarkMagenta";
		case 0x8B4513:
			return "SaddleBrown";
		case 0x8FBC8F:
			return "DarkSeaGreen";
		case 0x90EE90:
			return "LightGreen";
		case 0x9370DB:
			return "MediumPurple";
		case 0x9400D3:
			return "DarkViolet";
		case 0x98FB98:
			return "PaleGreen";
		case 0x9932CC:
			return "DarkOrchid";
		case 0x9ACD32:
			return "YellowGreen";
		case 0xA0522D:
			return "Sienna";
		case 0xA52A2A:
			return "Brown";
		case 0xA9A9A9:
			return "DarkGray";
		// return "DarkGrey";
		case 0xADD8E6:
			return "LightBlue";
		case 0xADFF2F:
			return "GreenYellow";
		case 0xAFEEEE:
			return "PaleTurquoise";
		case 0xB0C4DE:
			return "LightSteelBlue";
		case 0xB0E0E6:
			return "PowderBlue";
		case 0xB22222:
			return "FireBrick";
		case 0xB8860B:
			return "DarkGoldenRod";
		case 0xBA55D3:
			return "MediumOrchid";
		case 0xBC8F8F:
			return "RosyBrown";
		case 0xBDB76B:
			return "DarkKhaki";
		case 0xC0C0C0:
			return "Silver";
		case 0xC71585:
			return "MediumVioletRed";
		case 0xCD5C5C:
			return "IndianRed";
		case 0xCD853F:
			return "Peru";
		case 0xD2691E:
			return "Chocolate";
		case 0xD2B48C:
			return "Tan";
		case 0xD3D3D3:
			return "LightGray";
		// return "LightGrey";
		case 0xD8BFD8:
			return "Thistle";
		case 0xDA70D6:
			return "Orchid";
		case 0xDAA520:
			return "GoldenRod";
		case 0xDB7093:
			return "PaleVioletRed";
		case 0xDC143C:
			return "Crimson";
		case 0xDCDCDC:
			return "Gainsboro";
		case 0xDDA0DD:
			return "Plum";
		case 0xDEB887:
			return "BurlyWood";
		case 0xE0FFFF:
			return "LightCyan";
		case 0xE6E6FA:
			return "Lavender";
		case 0xE9967A:
			return "DarkSalmon";
		case 0xEE82EE:
			return "Violet";
		case 0xEEE8AA:
			return "PaleGoldenRod";
		case 0xF08080:
			return "LightCoral";
		case 0xF0E68C:
			return "Khaki";
		case 0xF0F8FF:
			return "AliceBlue";
		case 0xF0FFF0:
			return "HoneyDew";
		case 0xF0FFFF:
			return "Azure";
		case 0xF4A460:
			return "SandyBrown";
		case 0xF5DEB3:
			return "Wheat";
		case 0xF5F5DC:
			return "Beige";
		case 0xF5F5F5:
			return "WhiteSmoke";
		case 0xF5FFFA:
			return "MintCream";
		case 0xF8F8FF:
			return "GhostWhite";
		case 0xFA8072:
			return "Salmon";
		case 0xFAEBD7:
			return "AntiqueWhite";
		case 0xFAF0E6:
			return "Linen";
		case 0xFAFAD2:
			return "LightGoldenRodYellow";
		case 0xFDF5E6:
			return "OldLace";
		case 0xFF0000:
			return "Red";
		case 0xFF00FF:
			return "Magenta";
		// return "Fuchsia";
		case 0xFF1493:
			return "DeepPink";
		case 0xFF4500:
			return "OrangeRed";
		case 0xFF6347:
			return "Tomato";
		case 0xFF69B4:
			return "HotPink";
		case 0xFF7F50:
			return "Coral";
		case 0xFF8C00:
			return "DarkOrange";
		case 0xFFA07A:
			return "LightSalmon";
		case 0xFFA500:
			return "Orange";
		case 0xFFB6C1:
			return "LightPink";
		case 0xFFC0CB:
			return "Pink";
		case 0xFFD700:
			return "Gold";
		case 0xFFDAB9:
			return "PeachPuff";
		case 0xFFDEAD:
			return "NavajoWhite";
		case 0xFFE4B5:
			return "Moccasin";
		case 0xFFE4C4:
			return "Bisque";
		case 0xFFE4E1:
			return "MistyRose";
		case 0xFFEBCD:
			return "BlanchedAlmond";
		case 0xFFEFD5:
			return "PapayaWhip";
		case 0xFFF0F5:
			return "LavenderBlush";
		case 0xFFF5EE:
			return "SeaShell";
		case 0xFFF8DC:
			return "Cornsilk";
		case 0xFFFACD:
			return "LemonChiffon";
		case 0xFFFAF0:
			return "FloralWhite";
		case 0xFFFAFA:
			return "Snow";
		case 0xFFFF00:
			return "Yellow";
		case 0xFFFFE0:
			return "LightYellow";
		case 0xFFFFF0:
			return "Ivory";
		case 0xFFFFFF:
			return "White";
		default:
			return null;
		}
	}

	/**
	 * Função que converte uma sequência de caracteres que a identifica na linguagem
	 * de marcação HTML na cor correspondente. É a função inversa de
	 * {@link #color2html(Color)}.
	 * 
	 * @param html sequência de caracteres que designa a cor (seja o nome dela, seja
	 *             na forma rgb)
	 * @return cor correspondente
	 */
	public static Color html2color(String html) {
		int rgb = html2rgb(html);
		if (rgb == -1) // se não for o nome da cor, pode estar na forma rgb(X,Y,Z)
			return ColorUtils.html2color(html);
		else
			return ColorUtils.rgb2color(rgb);
	}

	/**
	 * Função que converte uma sequência de caracteres que a identifica na linguagem
	 * de marcação HTML num número inteiro que representa a cor. É a função inversa
	 * de {@link #rgb2html(int)}.
	 * 
	 * @param html nome da cor, tal como aceita na linguagem de marcação HTML
	 * @return inteiro correspondente entre 0 (preto) e 16777215 (branco, 0xFFFFFF)
	 */
	private static int html2rgb(String html) {
		switch (html) {
		case "Black":
			return 0x000000;
		case "Navy":
			return 0x000080;
		case "DarkBlue":
			return 0x00008B;
		case "MediumBlue":
			return 0x0000CD;
		case "Blue":
			return 0x0000FF;
		case "DarkGreen":
			return 0x006400;
		case "Green":
			return 0x008000;
		case "Teal":
			return 0x008080;
		case "DarkCyan":
			return 0x008B8B;
		case "DeepSkyBlue":
			return 0x00BFFF;
		case "DarkTurquoise":
			return 0x00CED1;
		case "MediumSpringGreen":
			return 0x00FA9A;
		case "Lime":
			return 0x00FF00;
		case "SpringGreen":
			return 0x00FF7F;
		case "Aqua":
			return 0x00FFFF;
		case "Cyan":
			return 0x00FFFF;
		case "MidnightBlue":
			return 0x191970;
		case "DodgerBlue":
			return 0x1E90FF;
		case "LightSeaGreen":
			return 0x20B2AA;
		case "ForestGreen":
			return 0x228B22;
		case "SeaGreen":
			return 0x2E8B57;
		case "DarkSlateGray":
			return 0x2F4F4F;
		case "DarkSlateGrey":
			return 0x2F4F4F;
		case "LimeGreen":
			return 0x32CD32;
		case "MediumSeaGreen":
			return 0x3CB371;
		case "Turquoise":
			return 0x40E0D0;
		case "RoyalBlue":
			return 0x4169E1;
		case "SteelBlue":
			return 0x4682B4;
		case "DarkSlateBlue":
			return 0x483D8B;
		case "MediumTurquoise":
			return 0x48D1CC;
		case "Indigo":
			return 0x4B0082;
		case "DarkOliveGreen":
			return 0x556B2F;
		case "CadetBlue":
			return 0x5F9EA0;
		case "CornflowerBlue":
			return 0x6495ED;
		case "RebeccaPurple":
			return 0x663399;
		case "MediumAquaMarine":
			return 0x66CDAA;
		case "DimGray":
			return 0x696969;
		case "DimGrey":
			return 0x696969;
		case "SlateBlue":
			return 0x6A5ACD;
		case "OliveDrab":
			return 0x6B8E23;
		case "SlateGray":
			return 0x708090;
		case "SlateGrey":
			return 0x708090;
		case "LightSlateGray":
			return 0x778899;
		case "LightSlateGrey":
			return 0x778899;
		case "MediumSlateBlue":
			return 0x7B68EE;
		case "LawnGreen":
			return 0x7CFC00;
		case "Chartreuse":
			return 0x7FFF00;
		case "Aquamarine":
			return 0x7FFFD4;
		case "Maroon":
			return 0x800000;
		case "Purple":
			return 0x800080;
		case "Olive":
			return 0x808000;
		case "Gray":
			return 0x808080;
		case "Grey":
			return 0x808080;
		case "SkyBlue":
			return 0x87CEEB;
		case "LightSkyBlue":
			return 0x87CEFA;
		case "BlueViolet":
			return 0x8A2BE2;
		case "DarkRed":
			return 0x8B0000;
		case "DarkMagenta":
			return 0x8B008B;
		case "SaddleBrown":
			return 0x8B4513;
		case "DarkSeaGreen":
			return 0x8FBC8F;
		case "LightGreen":
			return 0x90EE90;
		case "MediumPurple":
			return 0x9370DB;
		case "DarkViolet":
			return 0x9400D3;
		case "PaleGreen":
			return 0x98FB98;
		case "DarkOrchid":
			return 0x9932CC;
		case "YellowGreen":
			return 0x9ACD32;
		case "Sienna":
			return 0xA0522D;
		case "Brown":
			return 0xA52A2A;
		case "DarkGray":
			return 0xA9A9A9;
		case "DarkGrey":
			return 0xA9A9A9;
		case "LightBlue":
			return 0xADD8E6;
		case "GreenYellow":
			return 0xADFF2F;
		case "PaleTurquoise":
			return 0xAFEEEE;
		case "LightSteelBlue":
			return 0xB0C4DE;
		case "PowderBlue":
			return 0xB0E0E6;
		case "FireBrick":
			return 0xB22222;
		case "DarkGoldenRod":
			return 0xB8860B;
		case "MediumOrchid":
			return 0xBA55D3;
		case "RosyBrown":
			return 0xBC8F8F;
		case "DarkKhaki":
			return 0xBDB76B;
		case "Silver":
			return 0xC0C0C0;
		case "MediumVioletRed":
			return 0xC71585;
		case "IndianRed":
			return 0xCD5C5C;
		case "Peru":
			return 0xCD853F;
		case "Chocolate":
			return 0xD2691E;
		case "Tan":
			return 0xD2B48C;
		case "LightGray":
			return 0xD3D3D3;
		case "LightGrey":
			return 0xD3D3D3;
		case "Thistle":
			return 0xD8BFD8;
		case "Orchid":
			return 0xDA70D6;
		case "GoldenRod":
			return 0xDAA520;
		case "PaleVioletRed":
			return 0xDB7093;
		case "Crimson":
			return 0xDC143C;
		case "Gainsboro":
			return 0xDCDCDC;
		case "Plum":
			return 0xDDA0DD;
		case "BurlyWood":
			return 0xDEB887;
		case "LightCyan":
			return 0xE0FFFF;
		case "Lavender":
			return 0xE6E6FA;
		case "DarkSalmon":
			return 0xE9967A;
		case "Violet":
			return 0xEE82EE;
		case "PaleGoldenRod":
			return 0xEEE8AA;
		case "LightCoral":
			return 0xF08080;
		case "Khaki":
			return 0xF0E68C;
		case "AliceBlue":
			return 0xF0F8FF;
		case "HoneyDew":
			return 0xF0FFF0;
		case "Azure":
			return 0xF0FFFF;
		case "SandyBrown":
			return 0xF4A460;
		case "Wheat":
			return 0xF5DEB3;
		case "Beige":
			return 0xF5F5DC;
		case "WhiteSmoke":
			return 0xF5F5F5;
		case "MintCream":
			return 0xF5FFFA;
		case "GhostWhite":
			return 0xF8F8FF;
		case "Salmon":
			return 0xFA8072;
		case "AntiqueWhite":
			return 0xFAEBD7;
		case "Linen":
			return 0xFAF0E6;
		case "LightGoldenRodYellow":
			return 0xFAFAD2;
		case "OldLace":
			return 0xFDF5E6;
		case "Red":
			return 0xFF0000;
		case "Fuchsia":
			return 0xFF00FF;
		case "Magenta":
			return 0xFF00FF;
		case "DeepPink":
			return 0xFF1493;
		case "OrangeRed":
			return 0xFF4500;
		case "Tomato":
			return 0xFF6347;
		case "HotPink":
			return 0xFF69B4;
		case "Coral":
			return 0xFF7F50;
		case "DarkOrange":
			return 0xFF8C00;
		case "LightSalmon":
			return 0xFFA07A;
		case "Orange":
			return 0xFFA500;
		case "LightPink":
			return 0xFFB6C1;
		case "Pink":
			return 0xFFC0CB;
		case "Gold":
			return 0xFFD700;
		case "PeachPuff":
			return 0xFFDAB9;
		case "NavajoWhite":
			return 0xFFDEAD;
		case "Moccasin":
			return 0xFFE4B5;
		case "Bisque":
			return 0xFFE4C4;
		case "MistyRose":
			return 0xFFE4E1;
		case "BlanchedAlmond":
			return 0xFFEBCD;
		case "PapayaWhip":
			return 0xFFEFD5;
		case "LavenderBlush":
			return 0xFFF0F5;
		case "SeaShell":
			return 0xFFF5EE;
		case "Cornsilk":
			return 0xFFF8DC;
		case "LemonChiffon":
			return 0xFFFACD;
		case "FloralWhite":
			return 0xFFFAF0;
		case "Snow":
			return 0xFFFAFA;
		case "Yellow":
			return 0xFFFF00;
		case "LightYellow":
			return 0xFFFFE0;
		case "Ivory":
			return 0xFFFFF0;
		case "White":
			return 0xFFFFFF;
		default:
			return -1;
		}
	}

	// -------------------- HTML TABLES --------------------

	public static final String TABLE_OPENING_TAG = "<(table|TABLE).*?>", TABLE_CLOSING_TAG = "</(table|TABLE)>",
			ROW_OPENING_TAG = "<(tr|TR).*?>", ROW_CLOSING_TAG = "</(tr|TR)>",
			COLUMN = "<(t[dh]|T[DH]).*?>.*?</(t[dh]|T[DH])>";

	public static final Pattern PATTERN_TABLE = Pattern.compile(HTML.TABLE_OPENING_TAG + ".*?" + HTML.TABLE_CLOSING_TAG,
			Pattern.DOTALL),
			PATTERN_ROW = Pattern.compile(HTML.ROW_OPENING_TAG + ".*?" + HTML.ROW_CLOSING_TAG, Pattern.DOTALL),
			PATTERN_COLUMN = Pattern.compile(COLUMN, Pattern.DOTALL);

	public static final Pattern PATTERN_TABLE_OPENING_TAG = Pattern.compile(TABLE_OPENING_TAG),
			PATTERN_TABLE_CLOSING_TAG = Pattern.compile(TABLE_CLOSING_TAG),
			PATTERN_ROW_OPENING_TAG = Pattern.compile(ROW_OPENING_TAG),
			PATTERN_ROW_CLOSING_TAG = Pattern.compile(ROW_CLOSING_TAG);

	/**
	 * Função que procura no código HTML uma dada tabela e retorna o contéudo de uma
	 * célula, identificada pela numeração de sua coluna e linha
	 * 
	 * @param html        sequência de caracteres do código HTML contendo pelo menos
	 *                    uma tabela
	 * @param tableIndex  índice da tabela
	 * @param rowIndex    índice da linha
	 * @param columnIndex índice da coluna
	 * @return conteúdo da célula procurada, ou <code>null</code> se a tabela, a    
	 *             coluna ou a linha não existem
	 */
	public static String getCellContent(String html, int tableIndex, int rowIndex, int columnIndex) {

		// ======================== TABELA ========================

		int[] limiters = StringUtils.getLimits(html, 0, html.length(), tableIndex, PATTERN_TABLE_OPENING_TAG,
				PATTERN_TABLE_CLOSING_TAG);

		if (limiters == null)
			return null;

		// ======================== LINHA ========================

		limiters = StringUtils.getLimits(html, limiters[0], limiters[1], rowIndex, PATTERN_ROW_OPENING_TAG,
				PATTERN_ROW_CLOSING_TAG);

		if (limiters == null)
			return null;

		// ======================== COLUNA ========================

		String out = StringUtils.getContent(html, limiters[0], limiters[1], columnIndex, PATTERN_COLUMN);

		if (out == null)
			return null;
		else
			return out.replaceAll("<.+?>", "");
	}

	/**
	 * Função que procura no código HTML uma dada tabela e retorna o contéudo de uma
	 * coluna, identificada pela sua numeração
	 * 
	 * @param html        sequência de caracteres do código HTML contendo pelo menos
	 *                    uma tabela
	 * @param tableIndex  índice da tabela
	 * @param columnIndex índice da coluna
	 * @return vetor com o conteúdo das célula da coluna indicada
	 */
	public static String[] getColumnContent(String html, int tableIndex, int columnIndex) {
		// procurar no HTML inteiro onde começa e termina a tabela
		int[] limiters = StringUtils.getLimits(html, 0, html.length(), tableIndex, PATTERN_TABLE_OPENING_TAG,
				PATTERN_TABLE_CLOSING_TAG);

		if (limiters == null)
			return null;

		LinkedList<String> out = new LinkedList<>();
		int start = limiters[0];
		while (true) {
			// linha
			int[] ls = StringUtils.getLimits(html, start, limiters[1], 0, PATTERN_ROW_OPENING_TAG,
					PATTERN_ROW_CLOSING_TAG);

			if (ls == null)
				break;

			// ler coluna
			String cell = StringUtils.getContent(html, ls[0], ls[1], columnIndex, PATTERN_COLUMN);
			out.add(cell);

			// a próxima linha começa no final desta
			start = ls[1];
		}

		return out.toArray(new String[out.size()]);
	}

	/**
	 * Função que procura no código HTML uma dada tabela e retorna o contéudo de
	 * várias colunas, identificadas pelas sua numeração
	 * 
	 * @param html         sequência de caracteres do código HTML contendo pelo
	 *                     menos uma tabela
	 * @param tableIndex   índice da tabela
	 * @param columnsIndex vetor com os índices das colunas (vetor de tamanho zero
	 *                     para pegar todas as colunas)
	 * @return matriz com o conteúdo das célula das colunas indicadas
	 */
	public static String[][] getColumnsContent(String html, int tableIndex, int... columnsIndex) {
		return getColumnsContent(html, false, tableIndex, columnsIndex);
	}

	/**
	 * Função que procura no código HTML uma dada tabela e retorna o contéudo de
	 * várias colunas, identificadas pelas sua numeração
	 * 
	 * @param html         sequência de caracteres do código HTML contendo pelo
	 *                     menos uma tabela
	 * @param remove       <code>true</code> para remover os prefixos e sufixos
	 *                     <...> e </...>, <code>false</code> para manter
	 * @param tableIndex   índice da tabela
	 * @param columnsIndex vetor com os índices das colunas (vetor de tamanho zero
	 *                     para pegar todas as colunas)
	 * @return lista com o conteúdo das célula das colunas indicadas
	 */
	public static List<String[]> getColumnsContentL(String html, boolean remove, int tableIndex, int... columnsIndex) {
		// procurar no HTML inteiro onde começa e termina a tabela
		int[] limiters = StringUtils.getLimits(html, 0, html.length(), tableIndex, PATTERN_TABLE_OPENING_TAG,
				PATTERN_TABLE_CLOSING_TAG);

		if (limiters == null)
			return null;

		LinkedList<String[]> out = new LinkedList<>();
		int start = limiters[0];
		while (true) {
			// linha
			int[] ls = StringUtils.getLimits(html, start, limiters[1], 0, PATTERN_ROW_OPENING_TAG,
					PATTERN_ROW_CLOSING_TAG);

			if (ls == null)
				break;

			// ler colunas
			String[] row = null;
			if (columnsIndex.length > 0) {
				// somente algumas colunas
				row = new String[columnsIndex.length];

				for (int i = 0; i < columnsIndex.length; i++)
					row[i] = StringUtils.getContent(html, ls[0], ls[1], columnsIndex[i], PATTERN_COLUMN);
			} else {
				// todas colunas
				LinkedList<String> rl = new LinkedList<>();

				int i = 0;
				String cell = null;
				while ((cell = StringUtils.getContent(html, ls[0], ls[1], i++, PATTERN_COLUMN)) != null)
					rl.add(cell);

				row = rl.toArray(new String[rl.size()]);
			}
			out.add(row);

			// a próxima linha começa no final desta
			start = ls[1];
		}

		if (remove) {
			Iterator<String[]> it = out.iterator();
			while (it.hasNext()) {
				String[] r = it.next();
				for (int j = 0; j < r.length; j++) {
					Matcher m = PATTERN_CONTENT.matcher(r[j]);
					m.find();
					r[j] = m.group();
					r[j] = r[j].substring(1, r[j].length() - 1);
					if ("".equals(r[j]))
						r[j] = null;
				}
			}
		}

		return out;
	}

	/**
	 * Função que procura no código HTML uma dada tabela e retorna o contéudo de
	 * várias colunas, identificadas pelas sua numeração
	 * 
	 * @param html         sequência de caracteres do código HTML contendo pelo
	 *                     menos uma tabela
	 * @param remove       <code>true</code> para remover os prefixos e sufixos
	 *                     <...> e </...>, <code>false</code> para manter
	 * @param tableIndex   índice da tabela
	 * @param columnsIndex vetor com os índices das colunas (vetor de tamanho zero
	 *                     para pegar todas as colunas)
	 * @return matriz com o conteúdo das célula das colunas indicadas
	 */
	public static String[][] getColumnsContent(String html, boolean remove, int tableIndex, int... columnsIndex) {
		List<String[]> list = getColumnsContentL(html, remove, tableIndex, columnsIndex);
		return list.toArray(new String[list.size()][]);
	}

	// --------------------- TREE HTML ---------------------

	/**
	 * Função que transforma a sequência de caracteres do código HTML em uma árvore.
	 * É a função inversa {@link HTML#getHTMLfromTree(DefaultMutableTreeNode)
	 * dessa}.
	 * 
	 * @param html código HTML
	 * @return árvore que organiza o código HTML
	 */
	public static DefaultMutableTreeNode getTreeHTML(String html) {
		// remove-se os br (além de não serem balanceados, não eram usados no
		// 'passado' de maneira própria)
		html = html.replaceAll("<br(| " + ATTRIB + ")>", "\n\n");

		DefaultMutableTreeNode root = null;

		Matcher m = Pattern.compile(".*?<(|/)\\p{Alnum}+(| " + ATTRIB + ")>", Pattern.DOTALL).matcher(html);
		m.region(START.length(), html.length());
		while (m.find()) {
			String s = m.group();
			String[] textComm = s.split("<");

			// TEXTO
			String text = textComm[0].trim();
			if (!"".equals(text))
				root.add(new DefaultMutableTreeNode(text));

			// PRÓXIMO COMANDO
			String comm = textComm[1].substring(0, textComm[1].length() - 1);
			if (comm.charAt(0) == '/') {
				// volta um nível
				DefaultMutableTreeNode c = (DefaultMutableTreeNode) root.getParent();
				if (c == null)
					break;
				else
					root = c;
			} else {
				// sobe um nível
				int p = comm.indexOf(' ');
				String attrib = null;
				if (p != -1) {
					attrib = comm.substring(comm.indexOf(' ') + 1);
					comm = comm.substring(0, p);
				}
				DefaultMutableTreeNode c = new DefaultMutableTreeNode(new String[] { comm, attrib });
				if (root != null)
					root.add(c);
				root = c;
			}
		}
		return root;
	}

	public static void otimizarHTML(DefaultMutableTreeNode node) {
		Object obj = node.getUserObject();
		if (obj instanceof String[]) {
			// se for um comando...
			if (node.getChildCount() > 1) {
				// ver se uma função é possível entre seus filhos
				DefaultMutableTreeNode n1 = (DefaultMutableTreeNode) node.getChildAt(0);
				Object obj1 = n1.getUserObject();
				for (int i = 1; i < node.getChildCount();) {
					DefaultMutableTreeNode n2 = (DefaultMutableTreeNode) node.getChildAt(i);
					Object obj2 = n2.getUserObject();
					boolean forward = false;
					if (obj1 instanceof String[] && obj2 instanceof String[]) {
						String[] s1 = (String[]) obj1;
						String[] s2 = (String[]) obj2;
						if (s1[0].equals(s2[0])) {
							// mesmo comando
							if (!(s1[1] == null ^ s2[1] == null)) {

								if (s1[1] == null ? true : s1[1].equals(s2[1])) {
									// se ambos forem vazios OU iguais
									forward = true;
									// repassa os filhos de n2 para n1...
									while (n2.getChildCount() > 0)
										n1.add((DefaultMutableTreeNode) n2.getChildAt(0));
									// ... e remove n2
									node.remove(i);
								}

								if (forward ? false : s1[1] != null) {
									// ver se há atributos e valores em comum
									String[] attVal1 = s1[1].split("=");
									String[] attVal2 = s2[1].split("=");
									if (attVal1[0].equals(attVal2[0])) {
										// se for o mesmo atributo

										// remove aspas e último ponta-e-vírgula
										Set<String> vals1 = new HashSet<String>(Arrays.asList(
												attVal1[1].substring(1, attVal1[1].length() - 2).split(";( |)")));
										Set<String> vals2 = new HashSet<String>(Arrays.asList(
												attVal2[1].substring(1, attVal2[1].length() - 2).split(";( |)")));

										Set<String> intersec = ListUtils.intersection(vals1, vals2);
										if (intersec.size() > 0) {
											// há algo em comum
											forward = true;

											if (!intersec.equals(vals1)) {
												// se a intersecção não é 1,
												// então 1 não está contido em 2
												// -> split 1

												// diferença
												vals1.removeAll(intersec);
												String value = attVal1[0] + "=\"";
												for (String s : vals1)
													value += s + "; ";
												DefaultMutableTreeNode diff = new DefaultMutableTreeNode(new String[] {
														s1[0], value.substring(0, value.length() - 1) + "\"" });
												n1.insert(diff, 0);

												while (n1.getChildCount() > 1)
													diff.add((DefaultMutableTreeNode) n1.getChildAt(1));

												// intersecção
												value = attVal1[0] + "=\"";
												for (String s : intersec)
													value += s + "; ";
												s1[1] = value.substring(0, value.length() - 1) + "\"";
											}

											if (!intersec.equals(vals2)) {
												// se a intersecção não é 2,
												// então 2 não está contido em 1
												// -> split 2

												// diferença
												vals2.removeAll(intersec);
												String value = attVal2[0] + "=\"";
												for (String s : vals2)
													value += s + "; ";
												DefaultMutableTreeNode diff = new DefaultMutableTreeNode(new String[] {
														s2[0], value.substring(0, value.length() - 1) + "\"" });
												n2.insert(diff, 0);

												while (n2.getChildCount() > 1)
													diff.add((DefaultMutableTreeNode) n2.getChildAt(1));

												// intersecção
												value = attVal2[0] + "=\"";
												for (String s : intersec)
													value += s + "; ";
												s2[1] = value.substring(0, value.length() - 1) + "\"";
											}
										}
									}
								}
							}
						}
					}
					if (!forward) {
						// move o contador para frente
						n1 = n2;
						obj1 = obj2;
						i++;
					}
				}
			} else if (node.getChildCount() == 0) {
				// se for um comando que incide sobre texto nenhum, o nó se
				// remove
				((DefaultMutableTreeNode) node.getParent()).remove(node);
			}
			// inspecionar filhos
			for (int i = 0; i < node.getChildCount(); i++)
				otimizarHTML((DefaultMutableTreeNode) node.getChildAt(i));
		}
	}

	/**
	 * Função recursiva que transforma a árvore organizadora do código HTML no
	 * respectivo código. É a função inversa {@link HTML#getTreeHTML(String) dessa}.
	 * 
	 * @param node árvore que organiza o código HTML
	 * @return código HTML
	 */
	public static String getHTMLfromTree(DefaultMutableTreeNode node) {
		Object obj = node.getUserObject();
		if (obj instanceof String[]) {
			String[] comm = (String[]) obj;
			String s = String.format("<%s%s>\n", comm[0], comm[1] != null ? " " + comm[1] : "");
			for (int i = 0; i < node.getChildCount(); i++)
				s += getHTMLfromTree((DefaultMutableTreeNode) node.getChildAt(i));
			s += "</" + comm[0] + ">\n";
			if (s.startsWith("<html>"))
				s = HTML.START + "\n" + s;
			return s;
		} else
			return obj.toString() + "\n";
	}

	public static String toHTML(Vector<?> table) {
		String out = "<table>\n";
		for (Object o : table) {
			out += "<tr>";
			Vector<?> row = (Vector<?>) o;
			for (Object col : row)
				out += "<td>" + col.toString() + "</td>";
			out += ROW_CLOSING_TAG + "\n";
		}
		out += TABLE_CLOSING_TAG;
		return out;
	}

	public static String fromHTML(String text) {
		text = text.replaceAll("(<br(| /)>|<p/>|</p>)", "\r\n"); // TODO mas que porcaria...
		text = text.replaceAll("(<p>|<(|/)strong>)", "");
		text = text.replaceAll("&nbsp;", "\u00a0"); // non-breaking space
		return text;
	}
}