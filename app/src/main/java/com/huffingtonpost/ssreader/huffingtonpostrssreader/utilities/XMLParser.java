package com.huffingtonpost.ssreader.huffingtonpostrssreader.utilities;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;

import org.xml.sax.Attributes;

import android.util.Xml;

import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.Channel;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.Item;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.Items;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Zhisheng on 2015/6/29.
 */
public class XMLParser extends DefaultHandler {

    private Channel channel;
    private Items items;
    private Item item;

    public XMLParser() {
        items = new Items();
    }

    public Channel parse(String xmlString) {
        RootElement root = new RootElement("rss");
        Element chanElement = root.getChild("channel");
        Element chanTitle = chanElement.getChild("title");
        Element chanLink = chanElement.getChild("link");
        Element chanDescription = chanElement.getChild("description");

        Element chanItem = chanElement.getChild("item");
        Element itemGuid = chanItem.getChild("guid");
        Element itemTitle = chanItem.getChild("title");
        Element itemAuthor = chanItem.getChild("author");
        Element itemLink = chanItem.getChild("link");
        Element itemPubDate = chanItem.getChild("pubDate");
        Element itemDescription = chanItem.getChild("description");
        Element itemEnclosure = chanItem.getChild("enclosure");

        chanElement.setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attributes) {
                channel = new Channel();
            }
        });

        // Listen for the end of a text element and set the text as our
        // channel's title.
        chanTitle.setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                channel.setTitle(body);
            }
        });
        chanLink.setEndTextElementListener(new EndTextElementListener() {
            public void end(String link) {
                channel.setLink(link);
            }
        });
        chanDescription.setEndTextElementListener(new EndTextElementListener() {
            public void end(String description) {
                channel.setDescription(description);
            }
        });


        // On every </item> tag occurrence we add the current Item object
        // to the Items container.
        chanItem.setEndElementListener(new EndElementListener() {
            public void end() {
                items.add(item);
            }
        });

        itemGuid.setEndTextElementListener(new EndTextElementListener() {
            public void end(String guid) {
                item.setGuid(guid);
            }
        });
        itemTitle.setEndTextElementListener(new EndTextElementListener() {
            public void end(String title) {
                item.setTitle(title);
            }
        });
        itemAuthor.setEndTextElementListener(new EndTextElementListener() {
            public void end(String author) {
                item.setAuthor(author);
            }
        });
        itemLink.setEndTextElementListener(new EndTextElementListener() {
            public void end(String link) {
                item.setLink(link);
            }
        });
        itemPubDate.setEndTextElementListener(new EndTextElementListener() {
            public void end(String pubDate) {
                item.setPubDate(pubDate);
            }
        });

        itemDescription.setEndTextElementListener(new EndTextElementListener() {
            public void end(String description) {
                item.setDescription(description);
            }
        });

        itemEnclosure.setEndTextElementListener(new EndTextElementListener() {
            public void end(String enclosure) {
                item.setEnclosure(enclosure);
            }
        });

        // On every <item> tag occurrence we create a new Item object.
        chanItem.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                item = new Item();
            }
        });



        // here we actually parse the InputStream and return the resulting
        // Channel object.
        try {
            //InputStream stream = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));
            InputStream stream = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
            Xml.parse(stream, Xml.Encoding.UTF_8, root.getContentHandler());
            return channel;
        } catch (SAXException e) {
            // handle the exception
        } catch (IOException e) {
            // handle the exception
        }
        return null;
    }

}
