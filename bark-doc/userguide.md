



<!DOCTYPE html>
<html lang="en" class=" is-copy-enabled is-u2f-enabled">
  <head prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# object: http://ogp.me/ns/object# article: http://ogp.me/ns/article# profile: http://ogp.me/ns/profile#">
    <meta charset='utf-8'>

    <link crossorigin="anonymous" href="https://assets.github.corp.ebay.com/assets/frameworks-d6bbd7835ed17ba65665c66b35fd55d492334312f7a7e136abaa92df4c21cc07.css" integrity="sha256-1rvXg17Re6ZWZcZrNf1V1JIzQxL3p+E2q6qS30whzAc=" media="all" rel="stylesheet" />
    <link crossorigin="anonymous" href="https://assets.github.corp.ebay.com/assets/github-a96c60aa7783e74c0151d9621942006a25dbd064171ce42b9c9a8cf02318f155.css" integrity="sha256-qWxgqneD50wBUdliGUIAaiXb0GQXHOQrnJqM8CMY8VU=" media="all" rel="stylesheet" />
    
    
    
    

    <link as="script" href="https://assets.github.corp.ebay.com/assets/frameworks-d2c8ac50a1b74e9789dbe8f1d14b16507f2061f240c55ac8348824f70186bfd4.js" rel="preload" />
    <link as="script" href="https://assets.github.corp.ebay.com/assets/github-b6897db687c0fe2fb0458c57c5225b3c5e8fdcbc8e0feae73d3674481da8aadd.js" rel="preload" />

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Language" content="en">
    <meta name="viewport" content="width=1020">
    <meta content="origin-when-cross-origin" name="referrer" />
    
    <title>Bark/userguide.md at master · bark/Bark</title>
    <link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="GitHub">
    <link rel="fluid-icon" href="https://github.corp.ebay.com/fluidicon.png" title="GitHub">
    <link rel="apple-touch-icon" href="/apple-touch-icon.png">
    <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png">
    <link rel="apple-touch-icon" sizes="60x60" href="/apple-touch-icon-60x60.png">
    <link rel="apple-touch-icon" sizes="72x72" href="/apple-touch-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="76x76" href="/apple-touch-icon-76x76.png">
    <link rel="apple-touch-icon" sizes="114x114" href="/apple-touch-icon-114x114.png">
    <link rel="apple-touch-icon" sizes="120x120" href="/apple-touch-icon-120x120.png">
    <link rel="apple-touch-icon" sizes="144x144" href="/apple-touch-icon-144x144.png">
    <link rel="apple-touch-icon" sizes="152x152" href="/apple-touch-icon-152x152.png">
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon-180x180.png">
    <meta property="fb:app_id" content="1401488693436528">

      <meta content="https://avatars.github.corp.ebay.com/u/17300?s=400" name="twitter:image:src" /><meta content="@github" name="twitter:site" /><meta content="summary" name="twitter:card" /><meta content="bark/Bark" name="twitter:title" /><meta content="Bark - external open source version, under development" name="twitter:description" />
      <meta content="https://avatars.github.corp.ebay.com/u/17300?s=400" property="og:image" /><meta content="GitHub Enterprise" property="og:site_name" /><meta content="object" property="og:type" /><meta content="bark/Bark" property="og:title" /><meta content="https://github.corp.ebay.com/bark/Bark" property="og:url" /><meta content="Bark - external open source version, under development" property="og:description" />
    <meta name="browser-errors-url" content="https://github.corp.ebay.com/api/v3/_private/browser/errors">
    <link rel="assets" href="https://assets.github.corp.ebay.com/">
    <link rel="web-socket" href="wss://github.corp.ebay.com/_sockets/MTgxNzk6NDE2NDA0NWRmYTE5YzFhNGEzOGUxZmU4ZTRkNjk5Mzk6YTM2ZGQwNmQ3ZWZkN2JkYzFlZTZkNTkzNTYyMmQ3MDdlYWJlZWE5YmI2ZTQ3ODM2M2JiN2M0ODY4MjAyZmUwOQ==--473c98fed81db4e6bbe6da3ed4ff043fa288f77f">
    <meta name="pjax-timeout" content="1000">
    <link rel="sudo-modal" href="/sessions/sudo_modal">

    <meta name="msapplication-TileImage" content="/windows-tile.png">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="selected-link" value="repo_source" data-pjax-transient>

    <meta name="google-site-verification" content="KT5gs8h0wvaagLKAVWq8bbeNwnZZK1r1XQysX3xurLU">
<meta name="google-site-verification" content="ZzhVyEFwb7w3e0-uOTltm8Jsck2F5StVihD0exw2fsA">


<meta content="/&lt;user-name&gt;/&lt;repo-name&gt;/blob/show" data-pjax-transient="true" name="analytics-location" />



  <meta class="js-ga-set" name="dimension1" content="Logged In">



        <meta name="hostname" content="enterprise.host">
    <meta name="user-login" content="lliu13">


      <link rel="mask-icon" href="https://assets.github.corp.ebay.com/pinned-octocat.svg" color="#4078c0">
      <link rel="icon" type="image/x-icon" href="https://assets.github.corp.ebay.com/favicon-enterprise.ico">

    <meta content="4833a010032c53c8523f3c62bc8e2c9b70121a0b" name="form-nonce" />

    <meta http-equiv="x-pjax-version" content="9646c416ff7fb06dafab1be7f4a9383d">
    

      
  <meta name="description" content="Bark - external open source version, under development">
  <meta name="go-import" content="github.corp.ebay.com/bark/Bark git https://github.corp.ebay.com/bark/Bark.git">

  
  <link href="https://github.corp.ebay.com/bark/Bark/commits/master.atom?token=AABHA0irG17jOiACKuKl0CYsKMSYHZ4eks61pGpjwA%3D%3D" rel="alternate" title="Recent Commits to Bark:master" type="application/atom+xml">


      <link rel="canonical" href="https://github.corp.ebay.com/bark/Bark/blob/master/bark-doc/userguide.md" data-pjax-transient>
  </head>


  <body class="private-mode logged-in enterprise  env-production windows vis-public page-blob">
    <div id="js-pjax-loader-bar" class="pjax-loader-bar"></div>
    <a href="#start-of-content" tabindex="1" class="accessibility-aid js-skip-to-content">Skip to content</a>

    
    
    



        <div class="header header-logged-in true" role="banner">
  <div class="container clearfix">

    <a class="header-logo-invertocat" href="https://github.corp.ebay.com/" data-hotkey="g d" aria-label="Homepage" data-ga-click="Header, go to dashboard, icon:logo">
  <svg aria-hidden="true" class="octicon octicon-mark-github" height="28" role="img" version="1.1" viewBox="0 0 16 16" width="28"><path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59 0.4 0.07 0.55-0.17 0.55-0.38 0-0.19-0.01-0.82-0.01-1.49-2.01 0.37-2.53-0.49-2.69-0.94-0.09-0.23-0.48-0.94-0.82-1.13-0.28-0.15-0.68-0.52-0.01-0.53 0.63-0.01 1.08 0.58 1.23 0.82 0.72 1.21 1.87 0.87 2.33 0.66 0.07-0.52 0.28-0.87 0.51-1.07-1.78-0.2-3.64-0.89-3.64-3.95 0-0.87 0.31-1.59 0.82-2.15-0.08-0.2-0.36-1.02 0.08-2.12 0 0 0.67-0.21 2.2 0.82 0.64-0.18 1.32-0.27 2-0.27 0.68 0 1.36 0.09 2 0.27 1.53-1.04 2.2-0.82 2.2-0.82 0.44 1.1 0.16 1.92 0.08 2.12 0.51 0.56 0.82 1.27 0.82 2.15 0 3.07-1.87 3.75-3.65 3.95 0.29 0.25 0.54 0.73 0.54 1.48 0 1.07-0.01 1.93-0.01 2.2 0 0.21 0.15 0.46 0.55 0.38C13.71 14.53 16 11.53 16 8 16 3.58 12.42 0 8 0z"></path></svg>
</a>


        <div class="header-search scoped-search site-scoped-search js-site-search" role="search">
  <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/bark/Bark/search" class="js-site-search-form" data-scoped-search-url="/bark/Bark/search" data-unscoped-search-url="/search" method="get"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
    <label class="form-control header-search-wrapper js-chromeless-input-container">
      <div class="header-search-scope">This repository</div>
      <input type="text"
        class="form-control header-search-input js-site-search-focus js-site-search-field is-clearable"
        data-hotkey="s"
        name="q"
        placeholder="Search"
        aria-label="Search this repository"
        data-unscoped-placeholder="Search GitHub"
        data-scoped-placeholder="Search"
        tabindex="1"
        autocapitalize="off">
    </label>
</form></div>


      <ul class="header-nav left" role="navigation">
        <li class="header-nav-item">
          <a href="/pulls" class="js-selected-navigation-item header-nav-link" data-ga-click="Header, click, Nav menu - item:pulls context:user" data-hotkey="g p" data-selected-links="/pulls /pulls/assigned /pulls/mentioned /pulls">
            Pull requests
</a>        </li>
        <li class="header-nav-item">
          <a href="/issues" class="js-selected-navigation-item header-nav-link" data-ga-click="Header, click, Nav menu - item:issues context:user" data-hotkey="g i" data-selected-links="/issues /issues/assigned /issues/mentioned /issues">
            Issues
</a>        </li>
          <li class="header-nav-item">
            <a class="header-nav-link" href="https://gist.github.corp.ebay.com/" data-ga-click="Header, go to gist, text:gist">Gist</a>
          </li>
      </ul>

    
<ul class="header-nav user-nav right" id="user-links">
  <li class="header-nav-item">
    

  </li>

  <li class="header-nav-item dropdown js-menu-container">
    <a class="header-nav-link tooltipped tooltipped-s js-menu-target" href="/new"
       aria-label="Create new…"
       data-ga-click="Header, create new, icon:add">
      <svg aria-hidden="true" class="octicon octicon-plus left" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M12 9H7v5H5V9H0V7h5V2h2v5h5v2z"></path></svg>
      <span class="dropdown-caret"></span>
    </a>

    <div class="dropdown-menu-content js-menu-content">
      <ul class="dropdown-menu dropdown-menu-sw">
        
<a class="dropdown-item" href="/new" data-ga-click="Header, create new repository">
  New repository
</a>


  <a class="dropdown-item" href="/organizations/new" data-ga-click="Header, create new organization">
    New organization
  </a>

  <div class="dropdown-divider"></div>
  <div class="dropdown-header">
    <span title="bark">This organization</span>
  </div>
  <a class="dropdown-item" href="/orgs/bark/invitations/new" data-ga-click="Header, invite someone">
    Add someone
  </a>
  <a class="dropdown-item" href="/orgs/bark/new-team" data-ga-click="Header, create new team">
    New team
  </a>
  <a class="dropdown-item" href="/organizations/bark/repositories/new" data-ga-click="Header, create new organization repository, icon:repo">
    New repository
  </a>


  <div class="dropdown-divider"></div>
  <div class="dropdown-header">
    <span title="bark/Bark">This repository</span>
  </div>
    <a class="dropdown-item" href="/bark/Bark/issues/new" data-ga-click="Header, create new issue">
      New issue
    </a>
    <a class="dropdown-item" href="/bark/Bark/settings/collaboration" data-ga-click="Header, create new collaborator">
      New collaborator
    </a>

      </ul>
    </div>
  </li>

  <li class="header-nav-item dropdown js-menu-container">
    <a class="header-nav-link name tooltipped tooltipped-sw js-menu-target" href="/lliu13"
       aria-label="View profile and more"
       data-ga-click="Header, show menu, icon:avatar">
      <img alt="@lliu13" class="avatar" height="20" src="https://avatars.github.corp.ebay.com/u/18179?s=40" width="20" />
      <span class="dropdown-caret"></span>
    </a>

    <div class="dropdown-menu-content js-menu-content">
      <div class="dropdown-menu  dropdown-menu-sw">
        <div class=" dropdown-header header-nav-current-user css-truncate">
            Signed in as <strong class="css-truncate-target">lliu13</strong>

        </div>


        <div class="dropdown-divider"></div>

          <a class="dropdown-item" href="/lliu13" data-ga-click="Header, go to profile, text:your profile">
            Your profile
          </a>
        <a class="dropdown-item" href="/stars" data-ga-click="Header, go to starred repos, text:your stars">
          Your stars
        </a>
          <a class="dropdown-item" href="/explore" data-ga-click="Header, go to explore, text:explore">
            Explore
          </a>
        <a class="dropdown-item" href="https://help.github.com/enterprise/2.6/user" data-ga-click="Header, go to help, text:help">
          Help
        </a>


          <div class="dropdown-divider"></div>

          <a class="dropdown-item" href="/settings/profile" data-ga-click="Header, go to settings, icon:settings">
            Settings
          </a>

          <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/logout" class="logout-form" data-form-nonce="4833a010032c53c8523f3c62bc8e2c9b70121a0b" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="S7L3V23Ue2RYcDQD2NSnJRA6VaUds+jgwyUIldFhi4Vdxh4dXoL8ZhIzElG8xHdkGzkNcP50c1LBBS4peDWxQQ==" /></div>
            <button class="dropdown-item dropdown-signout" data-ga-click="Header, sign out, icon:logout">
              Sign out
            </button>
</form>
      </div>
    </div>
  </li>
</ul>


    
  </div>
</div>


      
<div class="flash flash-full js-notice flash-warn">
<div class="container">
















    <p>Github has been upgraded to <strong>2.6.4</strong>. Fore more information please refer <a href="https://wiki.vip.corp.ebay.com/x/n4dIFQ">https://wiki.vip.corp.ebay.com/x/n4dIFQ</a></p>

</div>
</div>


    <div id="start-of-content" class="accessibility-aid"></div>

      <div id="js-flash-container">
</div>


    <div role="main" class="main-content">
        <div itemscope itemtype="http://schema.org/SoftwareSourceCode">
    <div id="js-repo-pjax-container" data-pjax-container>
      
<div class="pagehead repohead instapaper_ignore readability-menu experiment-repo-nav">
  <div class="container repohead-details-container">

    

<ul class="pagehead-actions">

  <li>
        <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/notifications/subscribe" class="js-social-container" data-autosubmit="true" data-form-nonce="4833a010032c53c8523f3c62bc8e2c9b70121a0b" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="5l0Ey9eDxNvkimvIVG5aAfl9V0i7rLFU5Qa01sY0VdR4lQewOsobDyzQe1l2p75SOkxk0VNw55YQBZ282EEvhA==" /></div>      <input class="form-control" id="repository_id" name="repository_id" type="hidden" value="113840" />

        <div class="select-menu js-menu-container js-select-menu">
          <a href="/bark/Bark/subscription"
            class="btn btn-sm btn-with-count select-menu-button js-menu-target" role="button" tabindex="0" aria-haspopup="true"
            data-ga-click="Repository, click Watch settings, action:blob#show">
            <span class="js-select-button">
              <svg aria-hidden="true" class="octicon octicon-eye" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M8.06 2C3 2 0 8 0 8s3 6 8.06 6c4.94 0 7.94-6 7.94-6S13 2 8.06 2z m-0.06 10c-2.2 0-4-1.78-4-4 0-2.2 1.8-4 4-4 2.22 0 4 1.8 4 4 0 2.22-1.78 4-4 4z m2-4c0 1.11-0.89 2-2 2s-2-0.89-2-2 0.89-2 2-2 2 0.89 2 2z"></path></svg>
              Watch
            </span>
          </a>
          <a class="social-count js-social-count" href="/bark/Bark/watchers">
            5
          </a>

        <div class="select-menu-modal-holder">
          <div class="select-menu-modal subscription-menu-modal js-menu-content" aria-hidden="true">
            <div class="select-menu-header js-navigation-enable" tabindex="-1">
              <svg aria-label="Close" class="octicon octicon-x js-menu-close" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M7.48 8l3.75 3.75-1.48 1.48-3.75-3.75-3.75 3.75-1.48-1.48 3.75-3.75L0.77 4.25l1.48-1.48 3.75 3.75 3.75-3.75 1.48 1.48-3.75 3.75z"></path></svg>
              <span class="select-menu-title">Notifications</span>
            </div>

              <div class="select-menu-list js-navigation-container" role="menu">

                <div class="select-menu-item js-navigation-item selected" role="menuitem" tabindex="0">
                  <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M12 5L4 13 0 9l1.5-1.5 2.5 2.5 6.5-6.5 1.5 1.5z"></path></svg>
                  <div class="select-menu-item-text">
                    <input checked="checked" id="do_included" name="do" type="radio" value="included" />
                    <span class="select-menu-item-heading">Not watching</span>
                    <span class="description">Be notified when participating or @mentioned.</span>
                    <span class="js-select-button-text hidden-select-button-text">
                      <svg aria-hidden="true" class="octicon octicon-eye" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M8.06 2C3 2 0 8 0 8s3 6 8.06 6c4.94 0 7.94-6 7.94-6S13 2 8.06 2z m-0.06 10c-2.2 0-4-1.78-4-4 0-2.2 1.8-4 4-4 2.22 0 4 1.8 4 4 0 2.22-1.78 4-4 4z m2-4c0 1.11-0.89 2-2 2s-2-0.89-2-2 0.89-2 2-2 2 0.89 2 2z"></path></svg>
                      Watch
                    </span>
                  </div>
                </div>

                <div class="select-menu-item js-navigation-item " role="menuitem" tabindex="0">
                  <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M12 5L4 13 0 9l1.5-1.5 2.5 2.5 6.5-6.5 1.5 1.5z"></path></svg>
                  <div class="select-menu-item-text">
                    <input id="do_subscribed" name="do" type="radio" value="subscribed" />
                    <span class="select-menu-item-heading">Watching</span>
                    <span class="description">Be notified of all conversations.</span>
                    <span class="js-select-button-text hidden-select-button-text">
                      <svg aria-hidden="true" class="octicon octicon-eye" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M8.06 2C3 2 0 8 0 8s3 6 8.06 6c4.94 0 7.94-6 7.94-6S13 2 8.06 2z m-0.06 10c-2.2 0-4-1.78-4-4 0-2.2 1.8-4 4-4 2.22 0 4 1.8 4 4 0 2.22-1.78 4-4 4z m2-4c0 1.11-0.89 2-2 2s-2-0.89-2-2 0.89-2 2-2 2 0.89 2 2z"></path></svg>
                      Unwatch
                    </span>
                  </div>
                </div>

                <div class="select-menu-item js-navigation-item " role="menuitem" tabindex="0">
                  <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M12 5L4 13 0 9l1.5-1.5 2.5 2.5 6.5-6.5 1.5 1.5z"></path></svg>
                  <div class="select-menu-item-text">
                    <input id="do_ignore" name="do" type="radio" value="ignore" />
                    <span class="select-menu-item-heading">Ignoring</span>
                    <span class="description">Never be notified.</span>
                    <span class="js-select-button-text hidden-select-button-text">
                      <svg aria-hidden="true" class="octicon octicon-mute" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M8 2.81v10.38c0 0.67-0.81 1-1.28 0.53L3 10H1c-0.55 0-1-0.45-1-1V7c0-0.55 0.45-1 1-1h2l3.72-3.72c0.47-0.47 1.28-0.14 1.28 0.53z m7.53 3.22l-1.06-1.06-1.97 1.97-1.97-1.97-1.06 1.06 1.97 1.97-1.97 1.97 1.06 1.06 1.97-1.97 1.97 1.97 1.06-1.06-1.97-1.97 1.97-1.97z"></path></svg>
                      Stop ignoring
                    </span>
                  </div>
                </div>

              </div>

            </div>
          </div>
        </div>
</form>
  </li>

  <li>
    
  <div class="js-toggler-container js-social-container starring-container ">

    <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/bark/Bark/unstar" class="js-toggler-form starred" data-form-nonce="4833a010032c53c8523f3c62bc8e2c9b70121a0b" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="XYyIcwqfNQ6knyq8HCcyDGG9K4ZWj7yxMiyVE3VsefioQgA6G2KRNx4Yr+JMpwyg6giEyBywxmtwQag7E5rORg==" /></div>
      <button
        class="btn btn-sm btn-with-count js-toggler-target"
        aria-label="Unstar this repository" title="Unstar bark/Bark"
        data-ga-click="Repository, click unstar button, action:blob#show; text:Unstar">
        <svg aria-hidden="true" class="octicon octicon-star" height="16" role="img" version="1.1" viewBox="0 0 14 16" width="14"><path d="M14 6l-4.9-0.64L7 1 4.9 5.36 0 6l3.6 3.26L2.67 14l4.33-2.33 4.33 2.33L10.4 9.26 14 6z"></path></svg>
        Unstar
      </button>
        <a class="social-count js-social-count" href="/bark/Bark/stargazers">
          0
        </a>
</form>
    <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/bark/Bark/star" class="js-toggler-form unstarred" data-form-nonce="4833a010032c53c8523f3c62bc8e2c9b70121a0b" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="rtPYR/mh62XWndUgNSt9BV7xyuU27TFAFBlFuC5iiC+gxRsYkfe3E0YX0K0FdwYCoosgRmwaBt3vAsFQwjw34A==" /></div>
      <button
        class="btn btn-sm btn-with-count js-toggler-target"
        aria-label="Star this repository" title="Star bark/Bark"
        data-ga-click="Repository, click star button, action:blob#show; text:Star">
        <svg aria-hidden="true" class="octicon octicon-star" height="16" role="img" version="1.1" viewBox="0 0 14 16" width="14"><path d="M14 6l-4.9-0.64L7 1 4.9 5.36 0 6l3.6 3.26L2.67 14l4.33-2.33 4.33 2.33L10.4 9.26 14 6z"></path></svg>
        Star
      </button>
        <a class="social-count js-social-count" href="/bark/Bark/stargazers">
          0
        </a>
</form>  </div>

  </li>

  <li>
          <a href="#fork-destination-box" class="btn btn-sm btn-with-count"
              title="Fork your own copy of bark/Bark to your account"
              aria-label="Fork your own copy of bark/Bark to your account"
              rel="facebox"
              data-ga-click="Repository, show fork modal, action:blob#show; text:Fork">
              <svg aria-hidden="true" class="octicon octicon-repo-forked" height="16" role="img" version="1.1" viewBox="0 0 10 16" width="10"><path d="M8 1c-1.11 0-2 0.89-2 2 0 0.73 0.41 1.38 1 1.72v1.28L5 8 3 6v-1.28c0.59-0.34 1-0.98 1-1.72 0-1.11-0.89-2-2-2S0 1.89 0 3c0 0.73 0.41 1.38 1 1.72v1.78l3 3v1.78c-0.59 0.34-1 0.98-1 1.72 0 1.11 0.89 2 2 2s2-0.89 2-2c0-0.73-0.41-1.38-1-1.72V9.5l3-3V4.72c0.59-0.34 1-0.98 1-1.72 0-1.11-0.89-2-2-2zM2 4.2c-0.66 0-1.2-0.55-1.2-1.2s0.55-1.2 1.2-1.2 1.2 0.55 1.2 1.2-0.55 1.2-1.2 1.2z m3 10c-0.66 0-1.2-0.55-1.2-1.2s0.55-1.2 1.2-1.2 1.2 0.55 1.2 1.2-0.55 1.2-1.2 1.2z m3-10c-0.66 0-1.2-0.55-1.2-1.2s0.55-1.2 1.2-1.2 1.2 0.55 1.2 1.2-0.55 1.2-1.2 1.2z"></path></svg>
            Fork
          </a>

          <div id="fork-destination-box" style="display: none;">
            <h2 class="facebox-header" data-facebox-id="facebox-header">Where should we fork this repository?</h2>
            <include-fragment src=""
                class="js-fork-select-fragment fork-select-fragment"
                data-url="/bark/Bark/fork?fragment=1">
              <img alt="Loading" height="64" src="https://assets.github.corp.ebay.com/images/spinners/octocat-spinner-128.gif" width="64" />
            </include-fragment>
          </div>

    <a href="/bark/Bark/network" class="social-count">
      1
    </a>
  </li>
</ul>

    <h1 class="entry-title public ">
  <svg aria-hidden="true" class="octicon octicon-repo" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M4 9h-1v-1h1v1z m0-3h-1v1h1v-1z m0-2h-1v1h1v-1z m0-2h-1v1h1v-1z m8-1v12c0 0.55-0.45 1-1 1H6v2l-1.5-1.5-1.5 1.5V14H1c-0.55 0-1-0.45-1-1V1C0 0.45 0.45 0 1 0h10c0.55 0 1 0.45 1 1z m-1 10H1v2h2v-1h3v1h5V11z m0-10H2v9h9V1z"></path></svg>
  <span class="author" itemprop="author"><a href="/bark" class="url fn" rel="author">bark</a></span><!--
--><span class="path-divider">/</span><!--
--><strong itemprop="name"><a href="/bark/Bark" data-pjax="#js-repo-pjax-container">Bark</a></strong>

</h1>

  </div>
  <div class="container">
    
<nav class="reponav js-repo-nav js-sidenav-container-pjax"
     itemscope
     itemtype="http://schema.org/BreadcrumbList"
     role="navigation"
     data-pjax="#js-repo-pjax-container">

  <span itemscope itemtype="http://schema.org/ListItem" itemprop="itemListElement">
    <a href="/bark/Bark" aria-selected="true" class="js-selected-navigation-item selected reponav-item" data-hotkey="g c" data-selected-links="repo_source repo_downloads repo_commits repo_releases repo_tags repo_branches /bark/Bark" itemprop="url">
      <svg aria-hidden="true" class="octicon octicon-code" height="16" role="img" version="1.1" viewBox="0 0 14 16" width="14"><path d="M9.5 3l-1.5 1.5 3.5 3.5L8 11.5l1.5 1.5 4.5-5L9.5 3zM4.5 3L0 8l4.5 5 1.5-1.5L2.5 8l3.5-3.5L4.5 3z"></path></svg>
      <span itemprop="name">Code</span>
      <meta itemprop="position" content="1">
</a>  </span>

    <span itemscope itemtype="http://schema.org/ListItem" itemprop="itemListElement">
      <a href="/bark/Bark/issues" class="js-selected-navigation-item reponav-item" data-hotkey="g i" data-selected-links="repo_issues repo_labels repo_milestones /bark/Bark/issues" itemprop="url">
        <svg aria-hidden="true" class="octicon octicon-issue-opened" height="16" role="img" version="1.1" viewBox="0 0 14 16" width="14"><path d="M7 2.3c3.14 0 5.7 2.56 5.7 5.7S10.14 13.7 7 13.7 1.3 11.14 1.3 8s2.56-5.7 5.7-5.7m0-1.3C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7S10.86 1 7 1z m1 3H6v5h2V4z m0 6H6v2h2V10z"></path></svg>
        <span itemprop="name">Issues</span>
        <span class="counter">0</span>
        <meta itemprop="position" content="2">
</a>    </span>

  <span itemscope itemtype="http://schema.org/ListItem" itemprop="itemListElement">
    <a href="/bark/Bark/pulls" class="js-selected-navigation-item reponav-item" data-hotkey="g p" data-selected-links="repo_pulls /bark/Bark/pulls" itemprop="url">
      <svg aria-hidden="true" class="octicon octicon-git-pull-request" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M11 11.28c0-1.73 0-6.28 0-6.28-0.03-0.78-0.34-1.47-0.94-2.06s-1.28-0.91-2.06-0.94c0 0-1.02 0-1 0V0L4 3l3 3V4h1c0.27 0.02 0.48 0.11 0.69 0.31s0.3 0.42 0.31 0.69v6.28c-0.59 0.34-1 0.98-1 1.72 0 1.11 0.89 2 2 2s2-0.89 2-2c0-0.73-0.41-1.38-1-1.72z m-1 2.92c-0.66 0-1.2-0.55-1.2-1.2s0.55-1.2 1.2-1.2 1.2 0.55 1.2 1.2-0.55 1.2-1.2 1.2zM4 3c0-1.11-0.89-2-2-2S0 1.89 0 3c0 0.73 0.41 1.38 1 1.72 0 1.55 0 5.56 0 6.56-0.59 0.34-1 0.98-1 1.72 0 1.11 0.89 2 2 2s2-0.89 2-2c0-0.73-0.41-1.38-1-1.72V4.72c0.59-0.34 1-0.98 1-1.72z m-0.8 10c0 0.66-0.55 1.2-1.2 1.2s-1.2-0.55-1.2-1.2 0.55-1.2 1.2-1.2 1.2 0.55 1.2 1.2z m-1.2-8.8c-0.66 0-1.2-0.55-1.2-1.2s0.55-1.2 1.2-1.2 1.2 0.55 1.2 1.2-0.55 1.2-1.2 1.2z"></path></svg>
      <span itemprop="name">Pull requests</span>
      <span class="counter">0</span>
      <meta itemprop="position" content="3">
</a>  </span>

    <a href="/bark/Bark/wiki" class="js-selected-navigation-item reponav-item" data-hotkey="g w" data-selected-links="repo_wiki /bark/Bark/wiki">
      <svg aria-hidden="true" class="octicon octicon-book" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M2 5h4v1H2v-1z m0 3h4v-1H2v1z m0 2h4v-1H2v1z m11-5H9v1h4v-1z m0 2H9v1h4v-1z m0 2H9v1h4v-1z m2-6v9c0 0.55-0.45 1-1 1H8.5l-1 1-1-1H1c-0.55 0-1-0.45-1-1V3c0-0.55 0.45-1 1-1h5.5l1 1 1-1h5.5c0.55 0 1 0.45 1 1z m-8 0.5l-0.5-0.5H1v9h6V3.5z m7-0.5H8.5l-0.5 0.5v8.5h6V3z"></path></svg>
      Wiki
</a>
  <a href="/bark/Bark/pulse" class="js-selected-navigation-item reponav-item" data-selected-links="pulse /bark/Bark/pulse">
    <svg aria-hidden="true" class="octicon octicon-pulse" height="16" role="img" version="1.1" viewBox="0 0 14 16" width="14"><path d="M11.5 8L8.8 5.4 6.6 8.5 5.5 1.6 2.38 8H0V10h3.6L4.5 8.2l0.9 5.4L9 8.5l1.6 1.5H14V8H11.5z"></path></svg>
    Pulse
</a>
  <a href="/bark/Bark/graphs" class="js-selected-navigation-item reponav-item" data-selected-links="repo_graphs repo_contributors /bark/Bark/graphs">
    <svg aria-hidden="true" class="octicon octicon-graph" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M16 14v1H0V0h1v14h15z m-11-1H3V8h2v5z m4 0H7V3h2v10z m4 0H11V6h2v7z"></path></svg>
    Graphs
</a>
    <a href="/bark/Bark/settings" class="js-selected-navigation-item reponav-item" data-selected-links="repo_settings repo_branch_settings hooks /bark/Bark/settings">
      <svg aria-hidden="true" class="octicon octicon-gear" height="16" role="img" version="1.1" viewBox="0 0 14 16" width="14"><path d="M14 8.77V7.17l-1.94-0.64-0.45-1.09 0.88-1.84-1.13-1.13-1.81 0.91-1.09-0.45-0.69-1.92H6.17l-0.63 1.94-1.11 0.45-1.84-0.88-1.13 1.13 0.91 1.81-0.45 1.09L0 7.23v1.59l1.94 0.64 0.45 1.09-0.88 1.84 1.13 1.13 1.81-0.91 1.09 0.45 0.69 1.92h1.59l0.63-1.94 1.11-0.45 1.84 0.88 1.13-1.13-0.92-1.81 0.47-1.09 1.92-0.69zM7 11c-1.66 0-3-1.34-3-3s1.34-3 3-3 3 1.34 3 3-1.34 3-3 3z"></path></svg>
      Settings
</a>
</nav>

  </div>
</div>

<div class="container new-discussion-timeline experiment-repo-nav">
  <div class="repository-content">

    

<a href="/bark/Bark/blob/a11a43ce5ae65165ecc2b8ed3c59db532b7e241e/bark-doc/userguide.md" class="hidden js-permalink-shortcut" data-hotkey="y">Permalink</a>

<!-- blob contrib key: blob_contributors:v21:3467c787d0ebbd09b1d42ae778e72d2c -->

<div class="file-navigation js-zeroclipboard-container">
  
<div class="select-menu js-menu-container js-select-menu left">
  <button class="btn btn-sm select-menu-button js-menu-target css-truncate" data-hotkey="w"
    title="master"
    type="button" aria-label="Switch branches or tags" tabindex="0" aria-haspopup="true">
    <i>Branch:</i>
    <span class="js-select-button css-truncate-target">master</span>
  </button>

  <div class="select-menu-modal-holder js-menu-content js-navigation-container" data-pjax aria-hidden="true">

    <div class="select-menu-modal">
      <div class="select-menu-header">
        <svg aria-label="Close" class="octicon octicon-x js-menu-close" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M7.48 8l3.75 3.75-1.48 1.48-3.75-3.75-3.75 3.75-1.48-1.48 3.75-3.75L0.77 4.25l1.48-1.48 3.75 3.75 3.75-3.75 1.48 1.48-3.75 3.75z"></path></svg>
        <span class="select-menu-title">Switch branches/tags</span>
      </div>

      <div class="select-menu-filters">
        <div class="select-menu-text-filter">
          <input type="text" aria-label="Find or create a branch…" id="context-commitish-filter-field" class="form-control js-filterable-field js-navigation-enable" placeholder="Find or create a branch…">
        </div>
        <div class="select-menu-tabs">
          <ul>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="branches" data-filter-placeholder="Find or create a branch…" class="js-select-menu-tab" role="tab">Branches</a>
            </li>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="tags" data-filter-placeholder="Find a tag…" class="js-select-menu-tab" role="tab">Tags</a>
            </li>
          </ul>
        </div>
      </div>

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="branches" role="menu">

        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/bark/Bark/blob/elli/bark-doc/userguide.md"
               data-name="elli"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M12 5L4 13 0 9l1.5-1.5 2.5 2.5 6.5-6.5 1.5 1.5z"></path></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text" title="elli">
                elli
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open selected"
               href="/bark/Bark/blob/master/bark-doc/userguide.md"
               data-name="master"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M12 5L4 13 0 9l1.5-1.5 2.5 2.5 6.5-6.5 1.5 1.5z"></path></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text" title="master">
                master
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/bark/Bark/blob/weigao/bark-doc/userguide.md"
               data-name="weigao"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M12 5L4 13 0 9l1.5-1.5 2.5 2.5 6.5-6.5 1.5 1.5z"></path></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text" title="weigao">
                weigao
              </span>
            </a>
        </div>

          <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/bark/Bark/branches" class="js-create-branch select-menu-item select-menu-new-item-form js-navigation-item js-new-item-form" data-form-nonce="4833a010032c53c8523f3c62bc8e2c9b70121a0b" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="+lot8HIHaSDt8GgvqUjrxa1CQdCcj8YAt+E01lXkqcWw5NYnKnTybti3Mb3wIeLhsJ9MSh1bUi4htCRnrofalw==" /></div>
          <svg aria-hidden="true" class="octicon octicon-git-branch select-menu-item-icon" height="16" role="img" version="1.1" viewBox="0 0 10 16" width="10"><path d="M10 5c0-1.11-0.89-2-2-2s-2 0.89-2 2c0 0.73 0.41 1.38 1 1.72v0.3c-0.02 0.52-0.23 0.98-0.63 1.38s-0.86 0.61-1.38 0.63c-0.83 0.02-1.48 0.16-2 0.45V4.72c0.59-0.34 1-0.98 1-1.72 0-1.11-0.89-2-2-2S0 1.89 0 3c0 0.73 0.41 1.38 1 1.72v6.56C0.41 11.63 0 12.27 0 13c0 1.11 0.89 2 2 2s2-0.89 2-2c0-0.53-0.2-1-0.53-1.36 0.09-0.06 0.48-0.41 0.59-0.47 0.25-0.11 0.56-0.17 0.94-0.17 1.05-0.05 1.95-0.45 2.75-1.25s1.2-1.98 1.25-3.02h-0.02c0.61-0.36 1.02-1 1.02-1.73zM2 1.8c0.66 0 1.2 0.55 1.2 1.2s-0.55 1.2-1.2 1.2-1.2-0.55-1.2-1.2 0.55-1.2 1.2-1.2z m0 12.41c-0.66 0-1.2-0.55-1.2-1.2s0.55-1.2 1.2-1.2 1.2 0.55 1.2 1.2-0.55 1.2-1.2 1.2z m6-8c-0.66 0-1.2-0.55-1.2-1.2s0.55-1.2 1.2-1.2 1.2 0.55 1.2 1.2-0.55 1.2-1.2 1.2z"></path></svg>
            <div class="select-menu-item-text">
              <span class="select-menu-item-heading">Create branch: <span class="js-new-item-name"></span></span>
              <span class="description">from ‘master’</span>
            </div>
            <input type="hidden" name="name" id="name" class="js-new-item-value">
            <input type="hidden" name="branch" id="branch" value="master">
            <input type="hidden" name="path" id="path" value="bark-doc/userguide.md">
</form>
      </div>

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="tags">
        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


        </div>

        <div class="select-menu-no-results">Nothing to show</div>
      </div>

    </div>
  </div>
</div>

  <div class="btn-group right">
    <a href="/bark/Bark/find/master"
          class="js-pjax-capture-input btn btn-sm"
          data-pjax
          data-hotkey="t">
      Find file
    </a>
    <button aria-label="Copy file path to clipboard" class="js-zeroclipboard btn btn-sm zeroclipboard-button tooltipped tooltipped-s" data-copied-hint="Copied!" type="button">Copy path</button>
  </div>
  <div class="breadcrumb js-zeroclipboard-target">
    <span class="repo-root js-repo-root"><span class="js-path-segment"><a href="/bark/Bark"><span>Bark</span></a></span></span><span class="separator">/</span><span class="js-path-segment"><a href="/bark/Bark/tree/master/bark-doc"><span>bark-doc</span></a></span><span class="separator">/</span><strong class="final-path">userguide.md</strong>
  </div>
</div>


  <div class="commit-tease">
      <span class="right">
        <a class="commit-tease-sha" href="/bark/Bark/commit/1e9475ee3ff24d463251ef1e9aaaac86be312aa5" data-pjax>
          1e9475e
        </a>
        <time datetime="2016-07-26T02:12:19Z" is="relative-time">Jul 26, 2016</time>
      </span>
      <div>
        <img alt="@lzhixing" class="avatar" height="20" src="https://avatars.github.corp.ebay.com/u/16127?s=40" width="20" />
        <a href="/lzhixing" class="user-mention" rel="contributor">lzhixing</a>
          <a href="/bark/Bark/commit/1e9475ee3ff24d463251ef1e9aaaac86be312aa5" class="message" data-pjax="true" title="update userguide">update userguide</a>
      </div>

    <div class="commit-tease-contributors">
      <button type="button" class="btn-link muted-link contributors-toggle" data-facebox="#blob_contributors_box">
        <strong>2</strong>
         contributors
      </button>
          <a class="avatar-link tooltipped tooltipped-s" aria-label="qgao1" href="/bark/Bark/commits/master/bark-doc/userguide.md?author=qgao1"><img alt="@qgao1" class="avatar" height="20" src="https://avatars.github.corp.ebay.com/u/18258?s=40" width="20" /> </a>
    <a class="avatar-link tooltipped tooltipped-s" aria-label="lzhixing" href="/bark/Bark/commits/master/bark-doc/userguide.md?author=lzhixing"><img alt="@lzhixing" class="avatar" height="20" src="https://avatars.github.corp.ebay.com/u/16127?s=40" width="20" /> </a>


    </div>

    <div id="blob_contributors_box" style="display:none">
      <h2 class="facebox-header" data-facebox-id="facebox-header">Users who have contributed to this file</h2>
      <ul class="facebox-user-list" data-facebox-id="facebox-description">
          <li class="facebox-user-list-item">
            <img alt="@qgao1" height="24" src="https://avatars.github.corp.ebay.com/u/18258?s=48" width="24" />
            <a href="/qgao1">qgao1</a>
          </li>
          <li class="facebox-user-list-item">
            <img alt="@lzhixing" height="24" src="https://avatars.github.corp.ebay.com/u/16127?s=48" width="24" />
            <a href="/lzhixing">lzhixing</a>
          </li>
      </ul>
    </div>
  </div>

<div class="file">
  <div class="file-header">
  <div class="file-actions">

    <div class="btn-group">
      <a href="/bark/Bark/raw/master/bark-doc/userguide.md" class="btn btn-sm " id="raw-url">Raw</a>
        <a href="/bark/Bark/blame/master/bark-doc/userguide.md" class="btn btn-sm js-update-url-with-hash">Blame</a>
      <a href="/bark/Bark/commits/master/bark-doc/userguide.md" class="btn btn-sm " rel="nofollow">History</a>
    </div>

        <a class="btn-octicon tooltipped tooltipped-nw"
           href="https://windows.github.com"
           aria-label="Open this file in GitHub Desktop"
           data-ga-click="Repository, open with desktop, type:windows">
            <svg aria-hidden="true" class="octicon octicon-device-desktop" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M15 2H1c-0.55 0-1 0.45-1 1v9c0 0.55 0.45 1 1 1h5.34c-0.25 0.61-0.86 1.39-2.34 2h8c-1.48-0.61-2.09-1.39-2.34-2h5.34c0.55 0 1-0.45 1-1V3c0-0.55-0.45-1-1-1z m0 9H1V3h14v8z"></path></svg>
        </a>

        <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/bark/Bark/edit/master/bark-doc/userguide.md" class="inline-form js-update-url-with-hash" data-form-nonce="4833a010032c53c8523f3c62bc8e2c9b70121a0b" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="i0Wi6+RqYVhf/W6kIdgu2jxNaU+c5HqAW8/4deT3uw6VlHoFOt3FeBswyC3+2Y7j7Ikd45FqEklQRByIf6RPBw==" /></div>
          <button class="btn-octicon tooltipped tooltipped-nw" type="submit"
            aria-label="Edit this file" data-hotkey="e" data-disable-with>
            <svg aria-hidden="true" class="octicon octicon-pencil" height="16" role="img" version="1.1" viewBox="0 0 14 16" width="14"><path d="M0 12v3h3l8-8-3-3L0 12z m3 2H1V12h1v1h1v1z m10.3-9.3l-1.3 1.3-3-3 1.3-1.3c0.39-0.39 1.02-0.39 1.41 0l1.59 1.59c0.39 0.39 0.39 1.02 0 1.41z"></path></svg>
          </button>
</form>        <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="/bark/Bark/delete/master/bark-doc/userguide.md" class="inline-form" data-form-nonce="4833a010032c53c8523f3c62bc8e2c9b70121a0b" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="fdNDJLjnA0B2IIRbdxCK1wRPtUcTkFg9g0/CrXk/971XVzhILOMeFeVanF5qAxt16e39nSMarTvFi/qwTaLa4w==" /></div>
          <button class="btn-octicon btn-octicon-danger tooltipped tooltipped-nw" type="submit"
            aria-label="Delete this file" data-disable-with>
            <svg aria-hidden="true" class="octicon octicon-trashcan" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M10 2H8c0-0.55-0.45-1-1-1H4c-0.55 0-1 0.45-1 1H1c-0.55 0-1 0.45-1 1v1c0 0.55 0.45 1 1 1v9c0 0.55 0.45 1 1 1h7c0.55 0 1-0.45 1-1V5c0.55 0 1-0.45 1-1v-1c0-0.55-0.45-1-1-1z m-1 12H2V5h1v8h1V5h1v8h1V5h1v8h1V5h1v9z m1-10H1v-1h9v1z"></path></svg>
          </button>
</form>  </div>

  <div class="file-info">
      295 lines (176 sloc)
      <span class="file-info-divider"></span>
    11.2 KB
  </div>
</div>

  
  <div id="readme" class="readme blob instapaper_body">
    <article class="markdown-body entry-content" itemprop="text"><h1><a id="user-content-bark-user-guide" class="anchor" href="#bark-user-guide" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path></svg></a>Bark User Guide</h1>

<h2><a id="user-content-1-introduction--access" class="anchor" href="#1-introduction--access" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path></svg></a>1 Introduction &amp; Access</h2>

<ul>
<li>Bark is an open source Data Quality solution for distributed data systems at any scale in both streaming or batch data context.</li>
<li>You can reach Bark by going to the following URL : <a href="http://bark.qa.ebay.com/">http://bark.qa.ebay.com/</a></li>
<li>To log in the system, it requires your eBay NT credentials, username and password.The first time you log into the system you will be greeted with the homepage of BARK, you can choose "remember me" so you can access into the system directly the next time.</li>
<li>Users will primarily access this application from a PC.</li>
</ul>

<h2><a id="user-content-2-procedures" class="anchor" href="#2-procedures" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path></svg></a>2 Procedures</h2>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/Capture.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/Capture.PNG" alt="bpmn" style="max-width:100%;"></a></p>

<p>After you log into the system, you may follow the steps:</p>

<ol>
<li>First, create a new data asset.</li>
<li>Then, choose one model to process the data.</li>
<li>The heatmap will show all the data diagrams.</li>
<li>Finally, use the dashboard to show the data diagram you are interested in.</li>
</ol>

<h3><a id="user-content-21-data-asset" class="anchor" href="#21-data-asset" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path></svg></a>2.1 Data Asset</h3>

<p>First, you have to create a new data asset by clicking "DataAssets".</p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/data asset.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/data asset.PNG" style="max-width:100%;"></a></p>

<p>Click “Register Data Asset” to create a new record. The information required are listed below:</p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/asset.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/asset.PNG" style="max-width:100%;"></a></p>

<ol>
<li>Asset name<br>
Write a valid name into the blank.</li>
<li>Asset type<br>
There are mainly two types, inclouding hdfsfile and hivetable.</li>
<li>HDFS Path<br>
Input a valid path in the blank, example: /user/b_des/yosha/dr.</li>
<li>Platform<br>
There are several options provided: Bullseye, GPS, Hadoop, PDS, IDLS, Pulsar, Kafka, Sojourner, Sitespeed, EDW. Choose one platform from them.</li>
<li>Schema<br>
Enter name and type in the blank.</li>
</ol>

<h3><a id="user-content-22-create-model" class="anchor" href="#22-create-model" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path></svg></a>2.2 Create model</h3>

<p>By clicking "Models", and then choose"Create DQ Model". You can use the model to process data and get the result you want.</p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/create model.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/create model.PNG" style="max-width:100%;"></a></p>

<p>There are mainly four kinds of DQ models for you to choose, which are:</p>

<ol>
<li>if you want to measure the differencial rate between source and target, choose accuracy.</li>
<li>if you want to check the specific value of the data(such as:null column count), choose validity.</li>
<li>if you want to detect wrong data information, choose anomaly detection.</li>
<li>if you have already process the data by yourself, choose publish DQ metrics directly, POST it any time you want, and see the trend from data diagrams.</li>
</ol>

<h4><a id="user-content-221-accuracy" class="anchor" href="#221-accuracy" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path></svg></a><strong>2.2.1 Accuracy</strong></h4>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/accurancy.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/accurancy.PNG" style="max-width:100%;"></a></p>

<p><strong>Definition:</strong></p>

<p>Measured by how the values agree with an identified source of truth.</p>

<p><strong>Steps:</strong></p>

<p>1)      Choose source</p>

<p>Select the source dataset and fields which will be used for comparision.</p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/source.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/source.PNG" style="max-width:100%;"></a></p>

<p>For example, we choose 2 columns here.</p>

<p>2)      Choose target:</p>

<p>Select the target dataset and fields which will be used for comparision.</p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/target.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/target.PNG" style="max-width:100%;"></a></p>

<p>3)      Mapping source and target</p>

<ul>
<li>Step1:  On the left side of the page,select a prime key in “PK” column.</li>
<li>Step2:  Then, select whcih rule to match the source and the target. Here are 5 options to choose:

<ol>
<li>Exactly match: each column in the two columns should be 100% matched.</li>
<li>Length: only match whether the length of the two colums is the same.</li>
<li>Lower case: all the characters wiil be set in lower case, and then compare whether they are the same.</li>
<li>Upper case:all the characters wiil be set in upper case, and then compare whether they are the same.</li>
<li>Trim: all the blank space will be removed, and then compare whether they are the same.</li>
</ol></li>
<li>Step3:  Source fields:choose the which source that you want to compare with the target.</li>
</ul>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/333.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/333.PNG" style="max-width:100%;"></a></p>

<p>4)      Configuration</p>

<p>Set up the model required information.</p>

<p>Set a threshold, the value below it will be set into red, then you will know that the data has been changed while transmitting.</p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/confirm.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/confirm.PNG" style="max-width:100%;"></a></p>

<p>5)      Status</p>

<p>After you create a new accuracy model,there are three status:</p>

<ol>
<li><strong>Testing:</strong>This means the data is still in processing.</li>
<li><strong>Need verify:</strong>The data has already been processed, you can check the result by clicking the model's name.If you are satisified with the final result, you can click 'deploy' button to set the model into 'deployed' mode.</li>
<li><strong>Deployed:</strong>The model will be processed everyt time interval as you set in the schedule type.</li>
</ol>

<p><strong>Example:</strong></p>

<p>Suppose the source table A has 1000 records and the target table B only has 999 records which can perfectly match with A in selected fields, then the accuracy rate=999/1000*100%=99.9%.</p>

<h4><a id="user-content-222-validity" class="anchor" href="#222-validity" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path></svg></a><strong>2.2.2 Validity</strong></h4>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/validity.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/validity.PNG" style="max-width:100%;"></a></p>

<p><strong>Definition:</strong></p>

<p>Data is valid if it confirms to the system (format, type, values) of its definition.</p>

<p><strong>Steps:</strong></p>

<p>1)      Choose target</p>

<p>Select the target dataset and fields which you want to be checked.</p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/source2.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/source2.PNG" style="max-width:100%;"></a></p>

<p>2)      Define/select models</p>

<p>Define your syntax check logic which will be applied on the selected fields.</p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/sample.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/sample.PNG" style="max-width:100%;"></a></p>

<ul>
<li>Simple Statistics<br>
<strong>Null Count:</strong> null is a special marker used to indicate that a data value does not exist, this pattern statistics the amout of null columns.<br>
<strong>Unique Count:</strong> the unique constraint prevents two records from having identical values in a particular colum, this pattern statistics the amout of unique columns.<br>
<strong>Duplicate Count:</strong> duplicate count is the number of duplicate rows in a table and generate an output column that shows how many times each row occurs.</li>
<li>Summary Statistics<br>
<strong>Maximum:</strong> maximum is the bigggest value of the selected column.<br>
<strong>Minimum:</strong> minimum is the smallest value of the selected column.<br>
<strong>Mean:</strong> mean is the usual average of the selected column.<br>
<strong>Medium:</strong> medium is the middle value of the selected column.<br></li>
<li>Advanced Statistics<br>
In theoretical computer science and formal language theory, a regular expression (sometimes called a rational expression) is a sequence of characters that define a search pattern, mainly for use in pattern matching with strings, or string matching, i.e. "find and replace"-like operations.</li>
</ul>

<p>3)      Configurations</p>

<p>Set the basic information which are required for your model.</p>

<p>4)      Status</p>

<p>After you create a new validity model,there are three status:</p>

<ol>
<li><strong>Testing:</strong>This means the data is still in processing.</li>
<li><strong>Need verify:</strong>The data has already been processed, you can check the result by clicking the model's name.If you are satisified with the final result, you can click 'deploy' button to set the model into 'deployed' mode.</li>
<li><strong>Deployed:</strong>The model will be processed everyt time interval as you set in the schedule type.</li>
</ol>

<p><strong>Example1:</strong></p>

<p>Check the date scope of all records in Column A, which should be char(1) in one dataset.</p>

<p><strong>Example2:</strong></p>

<p>Check the data range (minimum, maximum) within a set of allowable values.</p>

<h4><a id="user-content-223-anomaly-detection" class="anchor" href="#223-anomaly-detection" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path></svg></a><strong>2.2.3 Anomaly Detection</strong></h4>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/anomaly .PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/anomaly .PNG" style="max-width:100%;"></a></p>

<p><strong>Definition:</strong></p>

<p>Identification of items, events or observations which do not confirm to an expected pattern or other items in a dataset.<br>
According to the threshold you set, the unusual data will be detected, system will send an e-mail notification to the users to remind them of the abnormal data.</p>

<p><strong>Steps:</strong></p>

<p>1)      Choose target</p>

<p>Select the target dataset and fields which you want to be checked.</p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/13.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/13.PNG" style="max-width:100%;"></a></p>

<p>2)      Define/select models</p>

<p>Select the pre-defined anomaly detection methods which will be applied on the selected fields.</p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/23.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/23.PNG" style="max-width:100%;"></a></p>

<ul>
<li><strong>History Trend Detection</strong><br>
This is a method of evaluating two or more measured events to compare the results at one time period with those from another time period(or series of time periods), on an yearly, monthly, weekly, daily or hourly basis. Currently weekly comparision is supported.</li>
<li><strong>Deviation Detection(Based on MAD)</strong><br>
Deviation detection algorithm usually look at a given time window of a metric time series and establish and upper and lower band of accepted deviations and assume any data point out of those band as a deviation.<br>
The incorrect data will be marked out on the diagram.</li>
<li><strong>Bollinger Bands Detection</strong><br>
Bollinger Bands are volatility bands placed above and below a moving average. Volatility is based on the standard deviation. which changes as volatility increases and decreases. The bands automatically widen when volatility increases and narrow when volatility decreases.</li>
</ul>

<p>3)      Configuration</p>

<p>Set the basic information which are required for your model.</p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/33.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/33.PNG" style="max-width:100%;"></a></p>

<p>4)      Status</p>

<p>After you create a new model,the status will be set in 'deployed' directly.</p>

<p><strong>Example1:</strong></p>

<p>Check the total row count changed day over day (or hour over hour) on one dataset.</p>

<p><strong>Example2:</strong></p>

<p>Check the values of all records in Column A should be matched with one expression pattern (first 3 characters should be ‘abc’) in one dataset.</p>

<h4><a id="user-content-224-publish-dq-metrics-directly" class="anchor" href="#224-publish-dq-metrics-directly" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path></svg></a><strong>2.2.4 Publish DQ Metrics Directly</strong></h4>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/p.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/p.PNG" style="max-width:100%;"></a></p>

<p><strong>Definition:</strong></p>

<p>This is used to publish DQ results which has already calculated offline by customers.</p>

<p><strong>Step:</strong></p>

<p>Configuration: Set the basic information which are required for your model.</p>

<p><strong>Method:</strong></p>

<p>POST</p>

<p><strong>Publish data format:</strong></p>

<p>Method: POST<br>
       Endpoint: <a href="https://bark.vip.ebay.com/api/v1/metrics">https://bark.vip.ebay.com/api/v1/metrics</a><br>
       Body:<br>
       {<br>
             "metricName": "",<br>
             "timestamp": 1463373496583,<br>
             "value": 99.9<br>
       }</p>

<p><strong>Status:</strong></p>

<p>After you create a new model,the status will be set in 'deployed' directly.</p>

<h2><a id="user-content-5-heatmap" class="anchor" href="#5-heatmap" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path></svg></a>5 Heatmap</h2>

<p>After the processing work has done, here are 3 ways to show the data diagram.   </p>

<ol>
<li><p>The blocks in green represent health and the blocks in red represent invalid.    </p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/Capwture.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/Capwture.PNG" style="max-width:100%;"></a></p></li>
<li><p>Clcik on "DQ Metrics"  </p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/data asset.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/data asset.PNG" style="max-width:100%;"></a>  </p>

<ul>
<li>You can see all the metrics diagrams are shown on the page.</li>
</ul>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/bullseye.png" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/bullseye.png" style="max-width:100%;"></a></p>

<ul>
<li>For the type accuracy, you can also download the sample data to do root cause analysis if the accuracy is dropped dramatically. By clicking 'Download sample',you may get a list of 20 recently added models, choose the one you want to download.</li>
</ul>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/download.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/download.PNG" style="max-width:100%;"></a></p>

<ul>
<li>By clicking on the diagram, you can get the zoom-in picture of it, and know the DQ metrics at the selected time window.<br></li>
</ul>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/DQ metirics.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/DQ metirics.PNG" style="max-width:100%;"></a></p></li>
<li><p>The metrics is shown on the right side of the page. By clicking on the number, you can get the diagram and details about the data.   </p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/side.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/side.PNG" style="max-width:100%;"></a></p></li>
</ol>

<h2><a id="user-content-6-my-dashboard" class="anchor" href="#6-my-dashboard" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path></svg></a>6 My Dashboard</h2>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/my dashboard.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/my dashboard.PNG" style="max-width:100%;"></a></p>

<p>User can subscribe a metrics so that it can be displayed on "my dashboard".  </p>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/subscribe.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/subscribe.PNG" style="max-width:100%;"></a></p>

<p>Subscribe steps:    </p>

<ol>
<li>Select Organization-&gt;Asset<br>
a) Can select on Organization level, then all the assets under this Organization will be selected<br>
b) Can select on Asset level<br>
c) Can multiple select<br></li>
<li>After the assets are selected, all the models under these assets shouldbe displayed, so that the end user will know which metrics he's selected</li>
<li>Do subscription, then you may get the result as follow.</li>
</ol>

<p><a href="/bark/Bark/blob/master/bark-doc/img/userguide/subscriberesult.PNG" target="_blank"><img src="/bark/Bark/raw/master/bark-doc/img/userguide/subscriberesult.PNG" style="max-width:100%;"></a></p>

<p>All the diagrams that you want to keep tracking are shown on 'my dashboard' page.</p>
</article>
  </div>

</div>

<button type="button" data-facebox="#jump-to-line" data-facebox-class="linejump" data-hotkey="l" class="hidden">Jump to Line</button>
<div id="jump-to-line" style="display:none">
  <!-- </textarea> --><!-- '"` --><form accept-charset="UTF-8" action="" class="js-jump-to-line-form" method="get"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
    <input class="form-control linejump-input js-jump-to-line-field" type="text" placeholder="Jump to line&hellip;" aria-label="Jump to line" autofocus>
    <button type="submit" class="btn">Go</button>
</form></div>

  </div>
  <div class="modal-backdrop"></div>
</div>


    </div>
  </div>

    </div>

        <div class="container site-footer-container">
  <div class="site-footer" role="contentinfo">
    <ul class="site-footer-links right">
      <li><a href="https://developer.github.com/enterprise/2.6" data-ga-click="Footer, go to api, text:api">API</a></li>
      <li><a href="https://training.github.com" data-ga-click="Footer, go to training, text:training">Training</a></li>
      <li><a href="https://shop.github.com" data-ga-click="Footer, go to shop, text:shop">Shop</a></li>
        <li><a href="https://github.com/blog">Blog</a></li>
        <li><a href="https://github.com/about">About</a></li>

    </ul>

    <a href="https://github.corp.ebay.com" aria-label="Homepage" class="site-footer-mark" title="GitHub Enterprise Version 2.6.4">
      <svg aria-hidden="true" class="octicon octicon-mark-github" height="24" role="img" version="1.1" viewBox="0 0 16 16" width="24"><path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59 0.4 0.07 0.55-0.17 0.55-0.38 0-0.19-0.01-0.82-0.01-1.49-2.01 0.37-2.53-0.49-2.69-0.94-0.09-0.23-0.48-0.94-0.82-1.13-0.28-0.15-0.68-0.52-0.01-0.53 0.63-0.01 1.08 0.58 1.23 0.82 0.72 1.21 1.87 0.87 2.33 0.66 0.07-0.52 0.28-0.87 0.51-1.07-1.78-0.2-3.64-0.89-3.64-3.95 0-0.87 0.31-1.59 0.82-2.15-0.08-0.2-0.36-1.02 0.08-2.12 0 0 0.67-0.21 2.2 0.82 0.64-0.18 1.32-0.27 2-0.27 0.68 0 1.36 0.09 2 0.27 1.53-1.04 2.2-0.82 2.2-0.82 0.44 1.1 0.16 1.92 0.08 2.12 0.51 0.56 0.82 1.27 0.82 2.15 0 3.07-1.87 3.75-3.65 3.95 0.29 0.25 0.54 0.73 0.54 1.48 0 1.07-0.01 1.93-0.01 2.2 0 0.21 0.15 0.46 0.55 0.38C13.71 14.53 16 11.53 16 8 16 3.58 12.42 0 8 0z"></path></svg>
</a>
    <ul class="site-footer-links">
      <li>&copy; 2016 <span title="0.09408s from localhost">GitHub</span>, Inc.</li>
        <li><a href="https://help.github.com/enterprise/2.6">Help</a></li>
        <li><a href="mailto:support@ebay.zendesk.com">Support</a></li>
    </ul>
  </div>
</div>



    
    

    <div id="ajax-error-message" class="ajax-error-message flash flash-error">
      <svg aria-hidden="true" class="octicon octicon-alert" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M15.72 12.5l-6.85-11.98C8.69 0.21 8.36 0.02 8 0.02s-0.69 0.19-0.87 0.5l-6.85 11.98c-0.18 0.31-0.18 0.69 0 1C0.47 13.81 0.8 14 1.15 14h13.7c0.36 0 0.69-0.19 0.86-0.5S15.89 12.81 15.72 12.5zM9 12H7V10h2V12zM9 9H7V5h2V9z"></path></svg>
      <button type="button" class="flash-close js-flash-close js-ajax-error-dismiss" aria-label="Dismiss error">
        <svg aria-hidden="true" class="octicon octicon-x" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M7.48 8l3.75 3.75-1.48 1.48-3.75-3.75-3.75 3.75-1.48-1.48 3.75-3.75L0.77 4.25l1.48-1.48 3.75 3.75 3.75-3.75 1.48 1.48-3.75 3.75z"></path></svg>
      </button>
      Something went wrong with that request. Please try again.
    </div>


      
      <script crossorigin="anonymous" integrity="sha256-0sisUKG3TpeJ2+jx0UsWUH8gYfJAxVrINIgk9wGGv9Q=" src="https://assets.github.corp.ebay.com/assets/frameworks-d2c8ac50a1b74e9789dbe8f1d14b16507f2061f240c55ac8348824f70186bfd4.js"></script>
      <script async="async" crossorigin="anonymous" integrity="sha256-tol9tofA/i+wRYxXxSJbPF6P3LyOD+rnPTZ0SB2oqt0=" src="https://assets.github.corp.ebay.com/assets/github-b6897db687c0fe2fb0458c57c5225b3c5e8fdcbc8e0feae73d3674481da8aadd.js"></script>
      
      
      
      
    <div class="js-stale-session-flash stale-session-flash flash flash-warn flash-banner hidden">
      <svg aria-hidden="true" class="octicon octicon-alert" height="16" role="img" version="1.1" viewBox="0 0 16 16" width="16"><path d="M15.72 12.5l-6.85-11.98C8.69 0.21 8.36 0.02 8 0.02s-0.69 0.19-0.87 0.5l-6.85 11.98c-0.18 0.31-0.18 0.69 0 1C0.47 13.81 0.8 14 1.15 14h13.7c0.36 0 0.69-0.19 0.86-0.5S15.89 12.81 15.72 12.5zM9 12H7V10h2V12zM9 9H7V5h2V9z"></path></svg>
      <span class="signed-in-tab-flash">You signed in with another tab or window. <a href="">Reload</a> to refresh your session.</span>
      <span class="signed-out-tab-flash">You signed out in another tab or window. <a href="">Reload</a> to refresh your session.</span>
    </div>
    <div class="facebox" id="facebox" style="display:none;">
  <div class="facebox-popup">
    <div class="facebox-content" role="dialog" aria-labelledby="facebox-header" aria-describedby="facebox-description">
    </div>
    <button type="button" class="facebox-close js-facebox-close" aria-label="Close modal">
      <svg aria-hidden="true" class="octicon octicon-x" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path d="M7.48 8l3.75 3.75-1.48 1.48-3.75-3.75-3.75 3.75-1.48-1.48 3.75-3.75L0.77 4.25l1.48-1.48 3.75 3.75 3.75-3.75 1.48 1.48-3.75 3.75z"></path></svg>
    </button>
  </div>
</div>

  </body>
</html>

